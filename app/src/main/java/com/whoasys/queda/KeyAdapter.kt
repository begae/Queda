package com.whoasys.queda

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getColor
import coil.load
import com.whoasys.queda.databinding.KeywordItemBinding
import com.whoasys.queda.entities.BUCKET
import com.whoasys.queda.entities.Keyword

class KeyAdapter(
    private val itemList : List<Keyword>
) : RecyclerView.Adapter<KeyAdapter.ViewHolder>() {
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
        val selected = listOf<Keyword>()

        holder.keywordName.text = item.value
        holder.keywordImage.load(BUCKET + item.value + ".png")

        holder.item.setOnClickListener {

            val green = getColor(Resources.getSystem(), R.color.teal_200, null)
            holder.keywordImage.setBackgroundColor(green)

            selected.plusElement(item)
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