package com.whoasys.queda.recyclers

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.whoasys.queda.R
import com.whoasys.queda.entities.Post
import com.whoasys.queda.entities.PostService

class StorePosts : Fragment() {

    private var authorId = "begae"
    private var postList: List<Post>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        sharedPref?.getString("manager_id", authorId)

        val networkThread = Thread {
            postList = PostService.call().getAllPostsBy(authorId).execute().body()
        }

        networkThread.start()
        networkThread.join()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.store_posts_list, container, false)

        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = StorePostsAdapter(postList?: emptyList())
            }
        }
        return view
    }
}