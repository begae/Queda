package com.whoasys.queda

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog

class StoreRegister : AppCompatActivity() {

    private lateinit var mEtAdress : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_register)


        mEtAdress = findViewById(R.id.storeAddress)
        mEtAdress.setOnClickListener {
            //주소 검색 웹뷰로 이동
            val intent = Intent(this, SearchActivity::class.java)
            getSearchResult.launch(intent)
        }

        val BusinessBtn = findViewById<Button>(R.id.BusinessBtn)
        BusinessBtn.setOnClickListener {
            val intent = Intent(this, BusinessRegister::class.java)
            startActivity(intent)
        }

        val nextBtn = findViewById<Button>(R.id.nextBtn)
        nextBtn.setOnClickListener {
            val intent = Intent(this, KeywordChoice::class.java)
            startActivity(intent)
        }

        var mon1=0
        var tue1=0
        var wen1=0
        var thu1=0
        var fri1=0
        var sat1=0
        var sun1=0

        val Mon = findViewById<Button>(R.id.Mon)
        Mon.setOnClickListener {
            if(mon1 == 0){
            Mon.setBackgroundResource(R.drawable.radius)
                ++mon1
            }
            else{
                Mon.setBackgroundResource(R.drawable.radius7)
                --mon1
            }
        }
        val Tue = findViewById<Button>(R.id.Tue)
        Tue.setOnClickListener {
            if(tue1 ==0) {
                Tue.setBackgroundResource(R.drawable.radius)
                ++tue1
            }
            else{
                Tue.setBackgroundResource(R.drawable.radius7)
                --tue1
            }
        }
        val Wen = findViewById<Button>(R.id.Wen)
        Wen.setOnClickListener {
            if(wen1 ==0) {
                Wen.setBackgroundResource(R.drawable.radius)
                ++wen1
            }
            else{
                Wen.setBackgroundResource(R.drawable.radius7)
                --wen1
            }
        }
        val Thu = findViewById<Button>(R.id.Thu)
        Thu.setOnClickListener {
            if(thu1 ==0) {
                Thu.setBackgroundResource(R.drawable.radius)
                ++thu1
            }
            else{
                Thu.setBackgroundResource(R.drawable.radius7)
                --thu1
            }
        }
        val Fri = findViewById<Button>(R.id.Fri)
        Fri.setOnClickListener {
            if(fri1 ==0) {
                Fri.setBackgroundResource(R.drawable.radius)
                ++fri1
            }
            else{
                Fri.setBackgroundResource(R.drawable.radius7)
                --fri1
            }
        }
        val Sat = findViewById<Button>(R.id.Sat)
        Sat.setOnClickListener {
            if(sat1 ==0) {
                Sat.setBackgroundResource(R.drawable.radius)
                ++sat1
            }
            else{
                Sat.setBackgroundResource(R.drawable.radius7)
                --sat1
            }
        }
        val Sun = findViewById<Button>(R.id.Sun)
        Sun.setOnClickListener {
            if(sun1 ==0) {
                Sun.setBackgroundResource(R.drawable.radius)
                ++sun1
            }
            else{
                Sun.setBackgroundResource(R.drawable.radius7)
                --sun1
            }
        }
    }


    override fun onBackPressed() {
        val builder= AlertDialog.Builder(this)
        builder.setTitle("알림")
        builder.setMessage("매장 등록을 취소하시겠습니까?")
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

    private val getSearchResult : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        //serach activtity 로부터의 결과 값이 이곳으로 전달된다.
        if(it.resultCode == RESULT_OK){
            if(it.data != null){
                val data = it.data!!.getStringExtra("data")
                mEtAdress.setText(data)
            }
        }
    }
}