package com.whoasys.queda.recyclers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.whoasys.queda.R
import com.whoasys.queda.databinding.StorePostsBinding
import com.whoasys.queda.entities.Post
import java.text.DateFormat
import java.util.*


class StorePostsAdapter(postList: List<Post>) : RecyclerView.Adapter<StorePostsAdapter.ViewHolder>() {

    private val list = postList

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
        holder.contentView.text = post.content
        val formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.KOREAN)
        val added = formatter.format(post.addedMillis)
        holder.addedView.text = added

        if (post.attached0 != null) {
            val bucket =
                "https://queda193930-ksrmac.s3.ap-northeast-2.amazonaws.com/public/"
            val key0 = post.attached0
            holder.thumbView.load(bucket + key0)
        }

        holder.item.setOnClickListener {

            val pair = Pair("post_id", post.id)
            val bundle = bundleOf(pair)
            //it.findNavController().navigate(R.id.action_st_to_postDetail, bundle)


        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(binding: StorePostsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = binding.storePostsItemTitle
        val contentView: TextView = binding.storePostsItemContent
        val addedView: TextView = binding.storePostsItemAdded
        val thumbView: ImageView = binding.thumbnail
        val item: TableRow = binding.storePostsItem
    }

}