package com.whoasys.queda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.whoasys.queda.entities.Post

class FeedAdapter(val itemList: ArrayList<Post>) :
    RecyclerView.Adapter<FeedAdapter.BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return BoardViewHolder(view)
    }

    //데이터와 아이템을 묶어준다
    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.tv_name.text = itemList[position].author.store!!.name
        holder.tv_location.text = itemList[position].author.store!!.address
        //holder.tv_time.text = itemList[position].addedMillis.toString()
        holder.tv_postTitle.text = itemList[position].title
        //val imgUrl =
        //holder.tv_postImg.setImageURI()
        holder.tv_postId.text = itemList[position].id.toString()
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }


    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_name = itemView.findViewById<TextView>(R.id.tv_name)
        val tv_location = itemView.findViewById<TextView>(R.id.tv_location)
        val tv_time = itemView.findViewById<TextView>(R.id.tv_time)
        val tv_postTitle = itemView.findViewById<TextView>(R.id.tv_postTitle)
        //val tv_postImg = itemView.findViewById<ImageView>(R.id.tv_postImg)
        val tv_postId = itemView.findViewById<TextView>(R.id.tv_postId)

        init {
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
    }

    var itemClickListener: OnItemClickListener? = null
}