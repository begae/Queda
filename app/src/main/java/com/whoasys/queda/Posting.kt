package com.whoasys.queda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.amplifyframework.core.Amplify
import com.whoasys.queda.databinding.PostingBinding
import com.whoasys.queda.entities.NetworkService
import com.whoasys.queda.entities.Post
import com.whoasys.queda.entities.User
import com.whoasys.queda.etc.getPath
import kotlinx.coroutines.launch
import java.io.File
import java.net.URL

class Posting : Fragment() {

    private lateinit var pickImages: ActivityResultLauncher<PickVisualMediaRequest>
    private var userId: String? = null
    private lateinit var networkThread: Thread
    private var urls: Array<URL?> = emptyArray()
    private var loggedIn: User? = null
    private lateinit var b: PostingBinding
    private var paths: Array<String> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString("user_id")
        }

        b = PostingBinding.inflate(layoutInflater)

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

                b.attachCheck.isChecked = false
            }
        }

        networkThread = Thread {
            loggedIn = NetworkService.call().find(userId?:"5").execute().body()
        }

        networkThread.start()
        networkThread.join()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val today = System.currentTimeMillis()
        var promoStart: String? = null
        var promoEnd: String? = null

        b.promoCheck.setOnCheckedChangeListener { _, _ ->

            if (b.promoCheck.isChecked) {
                b.datePickers.visibility = View.VISIBLE

                b.promoStartDate.minDate = today
                b.promoEndDate.minDate = today

                b.promoStartDate.setOnDateChangedListener { _, yyyy, mm, dd ->

                    val syyyy = yyyy.toString()
                    val smm = mm.toString().padStart(2, '0')
                    val sdd = dd.toString().padStart(2, '0')
                    promoStart = "$syyyy-$smm-$sdd"
                }

                b.promoEndDate.setOnDateChangedListener { _, yyyy, mm, dd ->

                    val eyyyy = yyyy.toString()
                    val emm = mm.toString().padStart(2, '0')
                    val edd = dd.toString().padStart(2, '0')
                    promoEnd = "$eyyyy-$emm-$edd"
                }

            } else {

                b.datePickers.visibility = View.GONE
                promoStart = null
                promoEnd = null
            }
        }

        b.attachCheck.setOnCheckedChangeListener { _, _ ->

            if (b.attachCheck.isChecked) {
                b.attachImages.visibility = View.VISIBLE

            } else {
                b.attachImages.visibility = View.GONE
            }
        }

        b.attachImages.setOnClickListener {

            pickImages.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        b.finishPostiing.setOnClickListener {

            if (b.promoCheck.isChecked) {

                if (promoStart.isNullOrEmpty() || promoEnd.isNullOrEmpty()) {
                    b.promoCheck.isChecked = false
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
                    b.postContent.text.toString(), b.promoCheck.isChecked,
                    promoStart, promoEnd
                )

                networkThread = Thread {
                    postId =
                        NetworkService.call().savePost(new).execute().body() ?: 2
                }

                networkThread.start()
                networkThread.join()

                if (b.attachCheck.isChecked) {

                    for (i: Int in paths.indices) {
                        viewLifecycleOwner.lifecycleScope.launch {

                            val now = System.currentTimeMillis()
                            val file = File(paths[i])
                            val url = uploadFile("${now.toString()}$i.jpeg", file)
                            urls = urls.plus(url)
                        }
                    }

                    networkThread = Thread {
                        postId = NetworkService.call().attachURLs(postId, urls.toString()).execute()
                            .body() ?: 3
                    }
                    networkThread.start()
                    networkThread.join()
                }

                val pair = Pair("id", postId)
                val bundle = bundleOf(pair)

                view?.findNavController()
                    ?.navigate(R.id.action_postingFragment_to_postFragment, bundle)
            }
        }

        return b.root
    }

    companion object {

        @JvmStatic
        fun newInstance(userId: String) =
            Posting().apply {
                arguments = Bundle().apply {
                    putString("user_id", userId)
                }
            }
    }

    private fun uploadFile(key: String, file: File): URL? {

        Amplify.Storage.uploadFile(key, file,
            { println("업로드에 성공했습니다: ${it.key}") },
            { println("업로드에 실패했습니다: $it") }
        )

        var url: URL? = null
        Amplify.Storage.getUrl(key,
            { url = it.url },
            { println("URL을 가져오지 못했습니다.") }
        )

        return url
    }
}