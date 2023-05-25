package com.whoasys.queda.recyclers

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.whoasys.queda.R
import com.whoasys.queda.entities.Post
import com.whoasys.queda.entities.PostService

class Feed : Fragment() {

    private lateinit var userId: String
    private var postList: List<Post>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        arguments?.let {
            userId = it.getString("user_id")?: "admin"
        }
         */
        userId = "kimsmj"

        /*if (userId == "admin") {
            view?.findNavController()
               */

        val networkThread = Thread {
                postList = PostService.call().getAllPostsNearby(userId).execute().body()
            }

        networkThread.start()
        networkThread.join()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.feed_item_list, container, false)

        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = FeedAdapter(postList?: emptyList(), userId)
            }
        }
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(userId: String) =
            Feed().apply {
                arguments = Bundle().apply {
                    putString("user_id", userId)
                }
            }
    }
}