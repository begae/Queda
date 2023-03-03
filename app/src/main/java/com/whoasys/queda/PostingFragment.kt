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
import com.whoasys.queda.databinding.FragmentPostingBinding
import com.whoasys.queda.entities.Post
import getPath
import kotlinx.coroutines.launch
import java.io.File
import java.net.URL

class PostingFragment : Fragment() {

    private lateinit var pickImages: ActivityResultLauncher<PickVisualMediaRequest>
    //private var idFromLogin: String? = null
    private lateinit var networkThread: Thread
    var urls: Array<URL?> = emptyArray()

    lateinit var b: FragmentPostingBinding
    var paths: Array<String> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //arguments?.let {
            //idFromLogin = it.getString("id")
        //}

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        b = FragmentPostingBinding.inflate(inflater, container, false)

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

                var postId = 0

                val new = Post(
                    b.postTitle.text.toString(), "ksr",
                    b.postContent.text.toString(), b.promoCheck.isChecked,
                    promoStart, promoEnd
                )

                networkThread = Thread {
                    postId =
                        NetworkService.call().savePost(new).execute().body() ?: -1
                }

                networkThread.start()
                networkThread.join()

                // TODO: postId를 0, -1, -2로 가지는 오류안내 포스트 만들어놓기

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
                            .body() ?: -2
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

    /*companion object {

        @JvmStatic
        fun newInstance(idFromLogin: String) =
            PostingFragment().apply {
                arguments = Bundle().apply {
                    putString("id", idFromLogin)
                }
            }
    }*/

    private suspend fun uploadFile(key: String, file: File): URL? {

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