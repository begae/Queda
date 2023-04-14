package com.whoasys.queda

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import com.whoasys.queda.databinding.ActivityVerifyBinding

class VerifyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val b = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.cancel.setOnClickListener {

            popUp(this, "매장 등록을 취소하시겠어요?")
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)
        }

        b.lookUp.setOnClickListener {

            if (b.bizNumber.text.isNotEmpty() && b.bizOwner.text.isNotEmpty() && b.bizFounded.text.isNotEmpty()) {
                // TODO: 홈택스 호출
                b.cancel.visibility = View.GONE
                b.continueBtn.visibility = View.VISIBLE
                Toast.makeText(this,"인증되었습니다.\n다음을 눌러 매장 등록을 완료해주세요.", Toast.LENGTH_LONG).show()
            }

            else {
                Toast.makeText(this,"입력이 누락되었습니다.\n다시 확인해주세요.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun popUp(activity: Activity, dialog: String) {

        val builder = android.app.AlertDialog.Builder(activity).setTitle("알림")
        builder.setMessage(dialog)
        builder.setCancelable(false)

        builder.setPositiveButton("네") { _, _ ->

            val dispatcher = this.onBackPressedDispatcher
            dispatcher.addCallback(this) {
                Toast.makeText(activity, "이전 화면으로 돌아갈게요.", Toast.LENGTH_LONG).show()
                finish()
            }
            dispatcher.onBackPressed()
        }

        builder.setNegativeButton("아니요") { _, _ -> }
        builder.show()
    }
}