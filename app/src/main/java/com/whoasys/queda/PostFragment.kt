package com.whoasys.queda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whoasys.queda.databinding.FragmentPostBinding
import java.text.DateFormat

class PostFragment : Fragment() {

    private var postId: Int = 0
    private var post: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getInt("id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val b = FragmentPostBinding.inflate(inflater, container, false)

        val networkThread = Thread {
            post = NetworkService.call().getOnePost(postId.toString()).execute().body()!!
        }

        networkThread.start()
        networkThread.join()

        if (post != null) {
            b.singlePostTitle.text = post!!.title
            b.singlePostAuthor.text = post!!.author.id
            b.singlePostContent.text = post!!.content
            val postDate = DateFormat.getDateInstance()
            val added = postDate.format(post!!.addedMillis)
            b.singlePostAdded.text = added
        }
        return b.root
    }

    companion object {

        @JvmStatic
        fun newInstance(postId: Int) =
            PostFragment().apply {
                arguments = Bundle().apply {
                    putInt("id", postId)
                }
            }
    }
}