package com.whoasys.queda.recyclers

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import coil.load
import com.whoasys.queda.R
import com.whoasys.queda.databinding.FeedItemBinding
import com.whoasys.queda.entities.BUCKET
import com.whoasys.queda.entities.Post
import java.text.DateFormat
import java.util.*

class FeedAdapter(
    private val itemList: List<Post>, private val userId: String
) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FeedItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val post = itemList[position]

        holder.storeName.text = post.author.store?.name
        holder.storeAddr.text = post.author.store?.address

        val formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.KOREAN)
        val added = formatter.format(post.addedMillis)
        holder.postAdded.text = added

        holder.postTitle.text = post.title

        if (post.attached0 != null) {

            holder.postThumb.visibility = View.VISIBLE

            val key0 = post.attached0
            holder.postThumb.load(BUCKET + key0)
        }

        else {

            holder.postThumb.visibility = View.GONE
        }

        holder.item.setOnClickListener {

            val pair0 = Pair("user_id", userId)
            val pair1 = Pair("post_id", post.id)
            val bundle = bundleOf(pair0, pair1)
            it.findNavController().navigate(R.id.action_feed_to_postDetail, bundle)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(binding: FeedItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val storeName = binding.feedItemStoreName
        val storeAddr = binding.feedItemStoreAddr
        val postAdded = binding.feedItemPostAdded
        val postTitle = binding.feedItemPostTitle
        val postThumb = binding.feedItemPostThumb
        val item = binding.feedItem
    }
}