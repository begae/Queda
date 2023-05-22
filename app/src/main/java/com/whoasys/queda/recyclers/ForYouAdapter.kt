package com.whoasys.queda.recyclers

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import coil.load
import com.whoasys.queda.R

import com.whoasys.queda.databinding.ForYouItemBinding
import com.whoasys.queda.entities.BUCKET
import com.whoasys.queda.entities.Store
import java.text.DateFormat
import java.util.Locale

class ForYouAdapter(
    private val itemList: List<Store>, private val userId: String
) : RecyclerView.Adapter<ForYouAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ForYouItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store = itemList[position]

        holder.storeName.text = store.name
        holder.storeAddr.text = store.address

        val formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.KOREAN)

        if (store.profilePic != null) {

            holder.storeThumb.visibility = View.VISIBLE

            val key0 = store.profilePic
            holder.storeThumb.load(BUCKET + key0)
        }

        else {

            holder.storeThumb.visibility = View.GONE
        }

        holder.item.setOnClickListener {
            //click event
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(binding: ForYouItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val storeName = binding.storeName
        val storeAddr = binding.storeAddr
        val storeKeyword = binding.storeKeyword
        val storeThumb = binding.storeThumb
        val item = binding.forYouItem
    }

}