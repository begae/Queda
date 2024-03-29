package com.whoasys.queda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StorageAccessLevel
import com.amplifyframework.storage.options.StorageUploadFileOptions.builder
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.whoasys.queda.databinding.PostingBinding
import com.whoasys.queda.entities.Post
import com.whoasys.queda.entities.PostService
import com.whoasys.queda.entities.User
import com.whoasys.queda.entities.UserService
import java.io.File

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

//TODO: 게시물 고유 ID 필드 , 이미지 첨부 필드 필요
class PostingModifying : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var pickImages: ActivityResultLauncher<PickVisualMediaRequest>
    private var userId: String? = "begae"
    private lateinit var networkThread: Thread
    private var keys: Array<String> = emptyArray()
    private var loggedIn: User? = null
    private lateinit var b: PostingBinding
    private var paths: Array<String> = emptyArray()
    private var postId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            postId = it.getInt("post_id", -1) // Get the post ID from arguments if available
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = PostingBinding.inflate(inflater, container, false)

        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.configure(requireContext())

            println("Amplify initialized.")
        } catch (error: AmplifyException) {
            println("Failed to initialize Amplify: $error")
        }

        pickImages = registerForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(3)
        ) { uris ->
            if (uris.isNotEmpty()) {
                for (i in uris.indices) {
                    val path = getPath(requireContext(), uris[i])
                    paths = paths.plus(path!!)
                }
            } else {
                b.attachCheckbox.isChecked = false
            }
        }

        networkThread = Thread {
            loggedIn = UserService.call().findUserById(userId ?: "admin").execute().body()
        }

        networkThread.start()
        networkThread.join()

        val today = System.currentTimeMillis()
        var promoStart: String? = null
        var promoEnd: String? = null

        b.promoCheckbox.setOnCheckedChangeListener { _, _ ->

            if (b.promoCheckbox.isChecked) {
                b.datePickers.visibility = View.VISIBLE

                b.promoStartDate.minDate = today
                b.promoEndDate.minDate = today

            } else {

                b.datePickers.visibility = View.GONE
                promoStart = null
                promoEnd = null
            }
        }

        b.promoStartDate.setOnDateChangedListener { _, yyyy, mm, dd ->

            val syyyy = yyyy.toString()
            val smm = mm.toString().padStart(2, '0')
            val sdd = dd.toString().padStart(2, '0')
            promoStart = "$syyyy. $smm. $sdd"
        }

        b.promoEndDate.setOnDateChangedListener { _, yyyy, mm, dd ->

            val eyyyy = yyyy.toString()
            val emm = mm.toString().padStart(2, '0')
            val edd = dd.toString().padStart(2, '0')
            promoEnd = "$eyyyy. $emm. $edd"
        }

        b.attachCheckbox.setOnCheckedChangeListener { _, _ ->

            if (b.attachCheckbox.isChecked) {
                b.attachImagesBtn.visibility = View.VISIBLE

            } else {
                b.attachImagesBtn.visibility = View.GONE
            }
        }

        b.attachImagesBtn.setOnClickListener {

            pickImages.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        // Rest of the code...

        b.finishPostiing.setOnClickListener {
            // Validation and update post logic...

            if (b.promoCheckbox.isChecked) {
                if (promoStart.isNullOrEmpty() || promoEnd.isNullOrEmpty()) {
                    b.promoCheckbox.isChecked = false
                    b.finishPostiing.isSelected = false
                    Toast.makeText(activity, "행사 시작일과 종료일을 설정해주세요.", Toast.LENGTH_LONG).show()
                }
            } else if (b.postTitle.text.isEmpty() || b.postContent.text.isEmpty()) {
                b.finishPostiing.isSelected = false
                Toast.makeText(activity, "제목과 내용을 입력해주세요.", Toast.LENGTH_LONG).show()
            } else {

                var postId = 1

                val updatedPost = Post(
                    b.postTitle.text.toString(), loggedIn!!,
                    b.postContent.text.toString(), b.promoCheckbox.isChecked,
                    promoStart, promoEnd
                )

                networkThread = Thread {
//                  postId = PostService.call().savePost(updatedPost).execute().body() ?: 2

                    //수정-- savePost<- 새로운 post를 저장하는 것이라, updatePost로 수정
                    PostService.call().updatePost(postId, updatedPost).execute()

                }

//                val updatedPost = Post(
//                    b.postTitle.text.toString(), loggedIn!!,
//                    b.postContent.text.toString(), b.promoCheckbox.isChecked,
//                    promoStart, promoEnd
//                )
//
//                networkThread = Thread {
//                    PostService.call().updatePost(postId, updatedPost).execute()
//                }

                networkThread.start()
                networkThread.join()

                if (b.attachCheckbox.isChecked) {
                    for (i: Int in paths.indices) {
                        val now = System.currentTimeMillis()
                        val file = File(paths[i])
                        val key = "${now.toString()}$i.jpeg"
                        uploadFile(key, file)
                        keys = keys.plus(key)
                    }

                    for (j: Int in keys.indices) {
                        networkThread = Thread {
                            PostService.call().attach(j, postId, keys[j]).execute()
                        }
                        networkThread.start()
                        networkThread.join()
                    }
                }

                val bundle = bundleOf("post_id" to postId)
                view?.findNavController()
                    ?.navigate(R.id.action_posting_to_postDetail, bundle)
            }
        }
        return b.root
    }

    private fun uploadFile(key: String, file: File) {
        val builder = builder().accessLevel(StorageAccessLevel.PUBLIC)
        val option = builder.build()
        Amplify.Storage.uploadFile(
            key, file, option,
            { println("업로드에 성공했습니다: ${it.key}") },
            { println("업로드에 실패했습니다: $it") }
        )
    }

}