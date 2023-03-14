package com.whoasys.queda

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class BusinessRegister : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.business_register)

        val backBtn = findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener {
            val intent = Intent(this, StoreRegister::class.java)
            startActivity(intent)
        }

        val businessNum = findViewById<EditText>(R.id.businessNum)
        val businessName = findViewById<EditText>(R.id.businessName)
        val openNum = findViewById<EditText>(R.id.openNum)
        val inquiryBtn = findViewById<Button>(R.id.inquiryBtn)
        inquiryBtn.setOnClickListener {
            if(businessNum.text.isNotEmpty() && businessName.text.isNotEmpty() && openNum.text.isNotEmpty()){
                Toast.makeText(this,"인증되었습니다.\n뒤로가기를 눌러 매장 등록을 완료해주세요.", Toast.LENGTH_LONG).show()
            }
            else if(businessNum.text.isEmpty() || businessName.text.isEmpty() || openNum.text.isEmpty()){
                Toast.makeText(this,"유효하지 않은 사업자입니다.\n다시 확인해주세요.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        val builder= AlertDialog.Builder(this)
        builder.setTitle("알림")
        builder.setMessage("사업자등록번호 조회을 취소하시겠습니까?")
        builder.setCancelable(false)
        builder.setPositiveButton("확인", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                exit()
            }
        })
        builder.setNegativeButton("뒤로", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {}
        })
        builder.show() // 다이얼로그 보이기
    }

    fun exit() { // 종료
        super.onBackPressed()
    }
}