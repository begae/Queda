package com.whoasys.queda

import android.app.PendingIntent.getActivity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.Navigation
import androidx.navigation.findNavController

class KeywordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyword)

        val userId = intent.getStringExtra(EXTRA_MESSAGE)


        val lookup = findViewById<Button>(R.id.look_up)
        lookup.setOnClickListener {
            val builder= AlertDialog.Builder(this)
            builder.setTitle("알림")
            builder.setMessage("키워드 선택 수정을 취소하시겠습니까?")
            builder.setCancelable(false)
            builder.setPositiveButton("확인", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.keywordActivity, MyPage())
                        .addToBackStack(null)
                        .commit()
                }
            })
            builder.setNegativeButton("뒤로", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {}
            })
            builder.show()
        }

        val checkbtn = findViewById<Button>(R.id.finish)
        checkbtn.setOnClickListener{
            val navController = Navigation.findNavController(this, R.id.keywordActivity)
            navController.navigate(R.id.Mypage)
        }


    }

    override fun onBackPressed() {
        // 다이얼로그 알람을 표시하기 위해 AlertDialog.Builder 클래스를 사용합니다.
        val builder = AlertDialog.Builder(this)

        // 알람에 표시할 메시지와 버튼을 설정합니다.
        builder.setMessage("정말로 종료하시겠습니까?")
            .setCancelable(false)
            .setPositiveButton("네") { dialog, id -> finish() }
            .setNegativeButton("아니요", null)

        // AlertDialog 객체를 생성하고 show() 메서드를 호출하여 알람을 표시합니다.
        val alert = builder.create()
        alert.show()
    }

}