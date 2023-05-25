package com.whoasys.queda

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import coil.load
import com.whoasys.queda.databinding.PostDetailBinding
import com.whoasys.queda.entities.BUCKET
import com.whoasys.queda.entities.LikesService
import com.whoasys.queda.entities.Post
import com.whoasys.queda.entities.PostService
import java.text.DateFormat
import java.util.*

class PostDetail : Fragment() {

    private var userId: String = "admin"
    private var postId: Int = 8
    private var storeId: Int = 1
    private var post: Post? = null
    private lateinit var networkThread: Thread

    private fun deletePost(postId: Int) {
        networkThread = Thread {
            Toast.makeText(requireContext(), "게시물이 삭제되었습니다.", Toast.LENGTH_SHORT).show()

            /*view?.findNavController()
                ?.navigate(R.id.action_postDetail_to_feed)*/
        }
        networkThread.start()
        }

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

                // TODO: 게시물 삭제, 수정, 공지로 설정 기능
            }

            else {

                b.visit.setOnClickListener {

                    storeId = post!!.author.store!!.id!!
                    val pair = Pair("store_id", storeId)
                    val bundle = bundleOf(pair)
                    view?.findNavController()
                        ?.navigate(R.id.action_postDetail_to_storeActivity, bundle)
                }

                b.scrap.setOnClickListener {

                    var flag: Boolean? = false

                    val networkThread = Thread {
                        flag = LikesService.call().scrap(userId, postId).execute().body()
                    }

                    networkThread.start()
                    networkThread.join()

                    if (flag == true) {
                        b.scrap.text = "스크랩 완료!"
                    }
                }
            }

            if (post!!.isPromo) {

                b.promoRange.visibility = View.VISIBLE
                b.promoStart.text = post!!.promoStart
                b.promoEnd.text = post!!.promoEnd
            }

            val key0 = post!!.attached0
            val key1 = post!!.attached1
            val key2 = post!!.attached2

            if (key0 != null) {
                b.image0.visibility = View.VISIBLE
                b.image0.load(BUCKET + key0)
            }
            if (key1 != null) {
                b.image1.visibility = View.VISIBLE
                b.image1.load(BUCKET + key1)
            }
            if (key2 != null) {
                b.image2.visibility = View.VISIBLE
                b.image2.load(BUCKET + key2)
            }

            b.titleView.text = post!!.title
            b.authorView.text = post!!.author.id
            b.contentView.text = post!!.content
            val formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.KOREAN)
            val added = formatter.format(post!!.addedMillis)
            b.addedView.text = added
        }


            b.modify.setOnClickListener {
                view?.findNavController()
                    ?.navigate(R.id.action_postDetail_to_postingModifying)
            }

            b.delete.setOnClickListener {
                if (postId != -1) {
                    // Confirm with the user before deleting the post
                    AlertDialog.Builder(requireContext())
                        .setTitle("알림")
                        .setMessage("정말 삭제하시겠습니까?")
                        .setPositiveButton("삭제하기") { _, _ ->
                            // Call the deletePost function
                            deletePost(postId)
                        }
                        .setNegativeButton("취소", null)
                        .show()
                } else {
                    Toast.makeText(activity, "유효하지않은 post ID", Toast.LENGTH_SHORT).show()
                }
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