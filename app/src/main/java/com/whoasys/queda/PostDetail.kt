package com.whoasys.queda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whoasys.queda.databinding.PostDetailBinding
import com.whoasys.queda.entities.NetworkService
import com.whoasys.queda.entities.Post
import java.text.DateFormat
import java.util.*

class PostDetail : Fragment() {

    private var userId: String = "admin"
    private var postId: Int = 1
    private var post: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getInt("post_id")
            userId = it.getString("user_id")?: "admin"
        }

        val networkThread = Thread {
            post = NetworkService.call().getOnePost(postId.toString()).execute().body()
        }

        networkThread.start()
        networkThread.join()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val b = PostDetailBinding.inflate(layoutInflater)

        if (post != null) {

            // coil implement

            if (post!!.author.id == userId) {

                b.customerMenu.visibility = View.GONE
                b.managerMenu.visibility = View.VISIBLE
            }

            b.postDetailTitle.text = post!!.title
            b.postDetailAuthor.text = post!!.author.id
            b.postDetailContent.text = post!!.content
            val formatter = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.KOREA)
            val added = formatter.format(post!!.addedMillis)
            b.postDetailAdded.text = added
        }

        return b.root
    }

    companion object {

        @JvmStatic
        fun newInstance(postId: Int, userId: String) =
            PostDetail().apply {
                arguments = Bundle().apply {
                    putInt("post_id", postId)
                    putString("user_id", userId)
                }
            }
    }
}