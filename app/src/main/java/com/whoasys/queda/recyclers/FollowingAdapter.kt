package com.whoasys.queda.recyclers

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.whoasys.queda.databinding.FollowingItemBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class FollowingAdapter(
    private val values: List<PlaceholderContent.PlaceholderItem>
) : RecyclerView.Adapter<FollowingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FollowingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        //holder.idView.text = item.id
       // holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FollowingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //val idView: TextView = binding.itemNumber
        //val contentView: TextView = binding.content

        //override fun toString(): String {
           // return super.toString() + " '" + contentView.text + "'"
        //}
    }

}