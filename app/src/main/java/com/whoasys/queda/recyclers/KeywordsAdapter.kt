package com.whoasys.queda.recyclers

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.whoasys.queda.databinding.KeywordItemBinding
import com.whoasys.queda.entities.BUCKET
import com.whoasys.queda.entities.Keyword

class KeywordsAdapter(
    private val itemList: List<Keyword>
) : RecyclerView.Adapter<KeywordsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            KeywordItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = itemList[position]

        holder.keywordName.text = item.value
        holder.keywordImage.load(BUCKET + item.value + ".png")

        holder.item.setOnClickListener {

            // TODO: List += item
        }


    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(binding: KeywordItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val keywordName = binding.keywordItemName
        val keywordImage = binding.keywordItemThumb
        val item = binding.keywordItem

    }
}