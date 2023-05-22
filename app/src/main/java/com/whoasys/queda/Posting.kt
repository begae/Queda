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
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.amplifyframework.storage.options.StorageUploadFileOptions.builder
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.whoasys.queda.databinding.PostingBinding
import com.whoasys.queda.entities.Post
import com.whoasys.queda.entities.PostService
import com.whoasys.queda.entities.User
import com.whoasys.queda.entities.UserService
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Posting.newInstance] factory method to
 * create an instance of this fragment.
 */
class Posting : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var pickImages: ActivityResultLauncher<PickVisualMediaRequest>
    private var userId: String? = "begae"
    private lateinit var networkThread: Thread
    private var keys: Array<String> = emptyArray()
    private var loggedIn: User? = null
    private lateinit var b: PostingBinding
    private var paths: Array<String> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        b = PostingBinding.inflate(inflater, container, false)

        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.configure(requireContext())

            println("앰플리파이를 기동했습니다.")
        } catch (error: AmplifyException) {
            println("앰플리파이를 기동하지 못했습니다: $error")
        }

        pickImages = registerForActivityResult(
            ActivityResultContracts
                .PickMultipleVisualMedia(3)
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

        b.finishPostiing.setOnClickListener {

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

                val new = Post(
                    b.postTitle.text.toString(), loggedIn!!,
                    b.postContent.text.toString(), b.promoCheckbox.isChecked,
                    promoStart, promoEnd
                )

                networkThread = Thread {
                    postId =
                        PostService.call().savePost(new).execute().body() ?: 2
                }

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

                val pair = Pair("post_id", postId)
                val bundle = bundleOf(pair)

                view?.findNavController()
                    ?.navigate(R.id.action_posting_to_postDetail, bundle)

            }
        }
        return b.root
    }

    private fun uploadFile(key: String, file: File) {

        val builder = builder().accessLevel(StorageAccessLevel.PUBLIC)
        val option = builder.build()
        Amplify.Storage.uploadFile(key, file, option,
            { println("업로드에 성공했습니다: ${it.key}") },
            { println("업로드에 실패했습니다: $it") }
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Posting.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Posting().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}