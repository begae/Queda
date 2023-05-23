package com.whoasys.queda.recyclers

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat.getColor
import coil.load
import com.whoasys.queda.R
import com.whoasys.queda.databinding.KeywordItemBinding
import com.whoasys.queda.entities.BUCKET
import com.whoasys.queda.entities.Keyword

class KeywordsAdapter(
    private val itemList: List<Keyword>
) : RecyclerView.Adapter<KeywordsAdapter.ViewHolder>() {

    // 단일 선택 및 다중 선택을 위한 변수 추가
    private val selectedItems: MutableList<Keyword> = mutableListOf()

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
            val white = getColor(Resources.getSystem(), R.color.white, null)

            // 아이템이 선택되었는지 확인
            val isSelected = selectedItems.contains(item)

            if (isSelected) {
                // 이미 선택된 아이템인 경우, 선택 해제 처리
                selectedItems.remove(item)
                holder.keywordName.setBackgroundColor(white)
                Toast.makeText(it.context, "${item.value}를/을 키워드로 취소했습니다.", Toast.LENGTH_SHORT).show()

            } else {
                // 선택되지 않은 아이템인 경우, 선택 처리
                selectedItems.add(item)
                holder.keywordName.setBackgroundColor(green)
                Toast.makeText(it.context, "${item.value}를/을 키워드로 선택했습니다.", Toast.LENGTH_SHORT).show()

            }

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