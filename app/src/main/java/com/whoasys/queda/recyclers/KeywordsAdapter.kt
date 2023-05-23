package com.whoasys.queda.recyclers

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

    // 다중 선택을 위한 MutableSet 사용
    private val selectedItems: MutableSet<Keyword> = mutableSetOf()

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

        // 아이템이 선택되었는지 확인하여 UI 업데이트
        if (selectedItems.contains(item)) {
            holder.keywordImage.setBackgroundColor(getColor(holder.itemView.context.resources, R.color.teal_200, null))
        }
        else {
            holder.keywordImage.setBackgroundColor(getColor(holder.itemView.context.resources, R.color.white, null))
        }

        holder.itemView.setOnClickListener {
            val isSelected = selectedItems.contains(item)

            if (isSelected) {
                selectedItems.remove(item)
                holder.keywordImage.setBackgroundColor(
                    getColor(holder.itemView.context.resources, R.color.white, null)
                )
                Toast.makeText(it.context, "${item.value}를/을 키워드로 취소했습니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                selectedItems.add(item)
                holder.keywordImage.setBackgroundColor(
                    getColor(holder.itemView.context.resources, R.color.teal_200, null)
                )
                Toast.makeText(it.context, "${item.value}를/을 키워드로 선택했습니다.", Toast.LENGTH_SHORT).show()
            }

            selected.plusElement(item)

            uploadSelectedKeywords() // 선택된 키워드 업로드
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

    private fun uploadSelectedKeywords() {
        // 선택된 키워드 업로드 처리
        val selectedKeywordList = selectedItems.toList()

        // TODO: 선택된 키워드를 업로드하는 로직을 구현하세요.
        // 선택된 키워드 목록인 selectedKeywordList를 활용하여 업로드 작업을 수행하세요.
        // 예를 들어, 선택된 키워드를 서버에 전송하거나 로컬 데이터베이스에 저장하는 등의 작업을 수행할 수 있습니다.
        // 이 부분은 애플리케이션의 요구사항에 맞게 구현되어야 합니다.
    }
}