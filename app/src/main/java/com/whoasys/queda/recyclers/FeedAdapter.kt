package com.whoasys.queda.recyclers

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import coil.load
import com.whoasys.queda.R
import com.whoasys.queda.databinding.FeedBinding
import com.whoasys.queda.entities.Post
import java.text.DateFormat
import java.util.*

class FeedAdapter(
    private val itemList: List<Post>
) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FeedBinding.inflate(
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
            val bucket =
                "https://queda193930-ksrmac.s3.ap-northeast-2.amazonaws.com/public/"
            val key0 = post.attached0
            holder.postThumb.load(bucket + key0)
        }

        holder.item.setOnClickListener {

            val pair = Pair("post_id", post.id)
            val bundle = bundleOf(pair)
            it.findNavController().navigate(R.id.action_feed_to_postDetail, bundle)
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class ViewHolder(binding: FeedBinding) : RecyclerView.ViewHolder(binding.root) {
        val storeName = binding.feedItemStoreName
        val storeAddr = binding.feedItemStoreAddr
        val postAdded = binding.feedItemPostAdded
        val postTitle = binding.feedItemPostTitle
        val postThumb = binding.feedItemPostThumb
        val item = binding.feedItem

        /*override fun toString(): String {
            return super.toString() //+ " '" + contentView.text + "'"
        }
        init {
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
    }

    var itemClickListener: OnItemClickListener? = null*/


    }
}