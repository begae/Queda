package com.whoasys.queda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.whoasys.queda.databinding.PostDetailBinding
import com.whoasys.queda.entities.Post
import com.whoasys.queda.entities.PostService
import java.text.DateFormat
import java.util.*

class PostDetail : Fragment() {

    private var userId: String = "admin"
    private var postId: Int = 8
    private var post: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getInt("post_id")
            userId = it.getString("user_id")?: "admin"
        }

        val networkThread = Thread {
            post = PostService.call().getOnePost(postId).execute().body()
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

            if (post!!.author.id == userId) {

                b.customerMenu.visibility = View.GONE
                b.managerMenu.visibility = View.VISIBLE
            }

            if (post!!.isPromo) {

                b.promoRange.visibility = View.VISIBLE
                b.promoStart.text = post!!.promoStart
                b.promoEnd.text = post!!.promoEnd
            }

            val bucket = "https://queda193930-ksrmac.s3.ap-northeast-2.amazonaws.com/public/"

            val key0 = post!!.attached0
            val key1 = post!!.attached1
            val key2 = post!!.attached2

            if (key0 != null) {
                b.image0.visibility = View.VISIBLE
                b.image0.load(bucket + key0)
            }
            if (key1 != null) {
                b.image1.visibility = View.VISIBLE
                b.image1.load(bucket + key1)
            }
            if (key2 != null) {
                b.image2.visibility = View.VISIBLE
                b.image2.load(bucket + key2)
            }

            b.titleView.text = post!!.title
            b.authorView.text = post!!.author.id
            b.contentView.text = post!!.content
            val formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.KOREAN)
            val added = formatter.format(post!!.addedMillis)
            b.addedView.text = added
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