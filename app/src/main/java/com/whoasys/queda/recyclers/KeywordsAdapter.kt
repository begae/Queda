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