package com.whoasys.queda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whoasys.queda.databinding.FragmentPostBinding
import com.whoasys.queda.entities.Post
import java.text.DateFormat

class PostFragment : Fragment() {

    private var postId: Int = 0
    lateinit var post: Post

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
            post = NetworkService.call().getOnePost(postId).execute().body()!!
        }

        networkThread.start()
        networkThread.join()

        b.postTitle.text = post.title
        b.postAuthor.text = post.author.id
        b.postContent.text = post.content
        val postDate = DateFormat.getDateInstance()
        postDate.format(post.addedMillis)
        b.postAdded.text = postDate.toString()

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