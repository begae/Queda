package com.whoasys.queda

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.amplifyframework.core.Amplify
import com.whoasys.queda.databinding.ActivityPostingBinding
import com.whoasys.queda.entities.Post
import com.whoasys.queda.entities.PostService
import com.whoasys.queda.entities.User
import com.whoasys.queda.entities.UserService
import java.io.File

class PostingActivity : AppCompatActivity() {

    private lateinit var pickImages: ActivityResultLauncher<PickVisualMediaRequest>
    private var userId: String? = "begae"
    private lateinit var networkThread: Thread
    private var keys: Array<String> = emptyArray()
    private var loggedIn: User? = null
    private lateinit var b: ActivityPostingBinding
    private var paths: Array<String> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPostingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pickImages = registerForActivityResult(
            ActivityResultContracts
                .PickMultipleVisualMedia(3)
        ) { uris ->
            if (uris.isNotEmpty()) {

                for (i in uris.indices) {

                    val path = getPath(this, uris[i])
                    paths = paths.plus(path!!)
                }

            } else {

                b.attachCheckbox.isChecked = false
            }
        }

        networkThread = Thread {
            loggedIn = UserService.call().findUserById(userId?:"admin").execute().body()
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
                    Toast.makeText(this, "행사 시작일과 종료일을 설정해주세요.", Toast.LENGTH_LONG).show()
                }

            } else if (b.postTitle.text.isEmpty() || b.postContent.text.isEmpty()) {

                b.finishPostiing.isSelected = false
                Toast.makeText(this, "제목과 내용을 입력해주세요.", Toast.LENGTH_LONG).show()

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

                //view?.findNavController()
                //    ?.navigate(R.id.action_posting_to_postDetail, bundle)
            }
        }
    }

    private fun uploadFile(key: String, file: File) {

        Amplify.Storage.uploadFile(key, file,
            { println("업로드에 성공했습니다: ${it.key}") },
            { println("업로드에 실패했습니다: $it") }
        )
    }
}