package com.whoasys.queda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class KeyAdapter() : RecyclerView.Adapter<KeyAdapter.ItemViewHolder>() {

    private lateinit var itemList : ArrayList<KeywordModel>

    constructor(itemList: ArrayList<KeywordModel>): this(){
        this.itemList = itemList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.keyword_items, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = itemList[position]

        holder.TextArea.text = item.name
        holder.ImageArea.setImageDrawable(item.img)
        holder.keyword_layout.setOnClickListener{
            Toast.makeText(it.context, "${item.name}를/을 키워드로 선택했습니다.", Toast.LENGTH_SHORT).show()
            setMultipleSelection(position)
        }

        if(item.selected){
            holder.itemView.setBackgroundResource(R.drawable.radius6)
        }
        else{
            holder.itemView.setBackgroundResource(R.drawable.radius5)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    private fun setMultipleSelection(position: Int){
        itemList[position].selected = !itemList[position].selected

        notifyItemChanged(position)
    }

    class ItemViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val keyword_layout: LinearLayout = itemView.findViewById(R.id.keyword_layout)
        val ImageArea :ImageView = itemView.findViewById(R.id.ImageArea)
        val TextArea: TextView = itemView.findViewById(R.id.TextArea)
    }

}