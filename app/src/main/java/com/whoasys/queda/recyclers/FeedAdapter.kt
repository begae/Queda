package com.whoasys.queda.recyclers

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.whoasys.queda.R
import com.whoasys.queda.recyclers.PlaceholderContent.PlaceholderItem
import com.whoasys.queda.databinding.FeedBinding
import com.whoasys.queda.entities.Post

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
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

        holder.feed_name.text = post.author.id
        holder.feed_location.text = post.title
        //holder.feed_time.text = itemList[position].addedMillis.toString()
        holder.feed_postTitle.text = post.content
        //val imgUrl =
        //holder.feed_postImg.setImageURI()
        holder.feed_postId.text = post.id.toString()
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class ViewHolder(binding: FeedBinding) : RecyclerView.ViewHolder(binding.root) {
        val feed_name = itemView.findViewById<TextView>(R.id.feed_name)
        val feed_location = itemView.findViewById<TextView>(R.id.feed_location)
        val feed_time = itemView.findViewById<TextView>(R.id.feed_time)
        val feed_postTitle = itemView.findViewById<TextView>(R.id.feed_postTitle)

        //val tv_postImg = itemView.findViewById<ImageView>(R.id.feed_postImg)
        val feed_postId = itemView.findViewById<TextView>(R.id.feed_postId)

        override fun toString(): String {
            return super.toString() //+ " '" + contentView.text + "'"
        }
        /*init {
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