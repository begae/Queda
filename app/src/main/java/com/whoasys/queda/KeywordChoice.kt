package com.whoasys.queda

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class KeywordChoice : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyword_choice)

        val listkey = findViewById<RecyclerView>(R.id.listkey)
        val adapter = KeyAdapter(getData())

        listkey.layoutManager = GridLayoutManager(this, 3)
        listkey.adapter = adapter

        val saveBtn = findViewById<Button>(R.id.saveBtn)
        saveBtn.setOnClickListener {
//            val intent = Intent(this, MyPage::class.java)
//            startActivity(intent)
            supportFragmentManager.beginTransaction()
                .replace(R.id.keywordchoice, MyPage())
                .addToBackStack(null)
                .commit()
        }



    }

    private fun getData(): ArrayList<KeywordModel>{
        val itemList: ArrayList<KeywordModel> = ArrayList()

//        itemList.add(KeywordModel(ContextCompat.getDrawable(this,R.drawable.coffee)!!, "차, 커피", false))
//        itemList.add(KeywordModel(ContextCompat.getDrawable(this,R.drawable.nail)!!, "네일아트", false))
//        itemList.add(KeywordModel(ContextCompat.getDrawable(this,R.drawable.health)!!, "헬스장", false))
//        itemList.add(KeywordModel(ContextCompat.getDrawable(this,R.drawable.store)!!, "편의점, 마트", false))
//        itemList.add(KeywordModel(ContextCompat.getDrawable(this,R.drawable.hair)!!, "미용실", false))
//        itemList.add(KeywordModel(ContextCompat.getDrawable(this,R.drawable.bathhouse)!!, "목욕탕", false))
//        itemList.add(KeywordModel(ContextCompat.getDrawable(this,R.drawable.diningroom)!!, "음식점", false))
//        itemList.add(KeywordModel(ContextCompat.getDrawable(this,R.drawable.shirt)!!, "옷가게", false))

        return itemList

    }


    override fun onBackPressed() {
        val builder= AlertDialog.Builder(this)
        builder.setTitle("알림")
        builder.setMessage("키워드 선택을 취소하시겠습니까?")
        builder.setCancelable(false)
        builder.setPositiveButton("확인", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                exit()
            }
        })
        builder.setNegativeButton("뒤로", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {}
        })
        builder.show()
    }

    fun exit() {
        super.onBackPressed()
    }
}