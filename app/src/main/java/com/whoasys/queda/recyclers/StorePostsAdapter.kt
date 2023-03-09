package com.whoasys.queda.recyclers

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.whoasys.queda.databinding.StorePostsBinding
import com.whoasys.queda.entities.Post
import java.text.DateFormat

class StorePostsAdapter(postIterable: Iterable<Post>) : RecyclerView.Adapter<StorePostsAdapter.ViewHolder>() {

    private val list = postIterable.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            StorePostsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val post = list[i]
        holder.titleView.text = post.title
        val formatter = DateFormat.getDateTimeInstance()
        val added = formatter.format(post.addedMillis)
        holder.addedView.text = added

        if (post.attached0 != null) {
            val bucket =
                "https://whoasys-queda-bucket-real193930-ksrmac.s3.ap-northeast-2.amazonaws.com/public/"
            val key0 = post.attached0
            holder.imageView.load(bucket + key0)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(binding: StorePostsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = binding.storePostsItemTitle
        val addedView: TextView = binding.storePostsItemAdded
        val imageView: ImageView = binding.thumbnail
    }

}