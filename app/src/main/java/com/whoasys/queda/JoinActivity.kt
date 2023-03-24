package com.whoasys.queda

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.whoasys.queda.databinding.ActivityJoinBinding
import com.whoasys.queda.entities.User
import com.whoasys.queda.entities.UserService

class JoinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val b = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(b.root)

        var loggedIn: User? = null

        val bundle = intent.getBundleExtra(EXTRA_MESSAGE)
        val idFromLogin = bundle?.getString("entered_id")
        val pwFromLogin = bundle?.getString("entered_pw")

        b.id.setText(idFromLogin)
        b.pw.setText(pwFromLogin)

        b.cancel.setOnClickListener {

            popUp(this, "회원가입을 취소하시겠어요?")
        }

        b.joinReal.setOnClickListener {

            if (b.id.text.isEmpty() || b.pw.text.isEmpty()
                || b.name.text.isEmpty() || b.email.text.isEmpty()) {

                b.joinReal.isSelected = false
                Toast.makeText(this, "필수 정보를 전부 입력해주세요.", Toast.LENGTH_LONG).show()
            }

            else if (!b.personalInfoAgree.isChecked) {

                b.joinReal.isSelected = false
                Toast.makeText(this, "약관에 동의해 주세요.", Toast.LENGTH_LONG).show()
            }

            else {

                val networkThread = Thread {

                    val new = User(b.id.text.toString(), b.pw.text.toString(),
                                b.name.text.toString(), b.email.text.toString(),
                                null, null)
                    loggedIn = UserService.call().join(new).execute().body()
                }

                networkThread.start()
                networkThread.join()

                if (loggedIn != null) {

                    val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
                    sharedPref?.edit {
                        putString("user_id", loggedIn!!.id)
                        putString("user_name", loggedIn!!.name)
                        putBoolean("user_is_manager", false)
                        putInt("user_store", 0)
                        apply()
                    }

                    val userId = loggedIn!!.id
                    val intent = Intent(this, KeywordActivity::class.java).apply {
                        putExtra(EXTRA_MESSAGE, userId)
                    }
                    startActivity(intent)

                } else {

                    b.joinReal.isSelected = false
                    Toast.makeText(this, "동일한 아이디가 존재해요.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun popUp(activity: Activity, dialog: String) {

        val builder = AlertDialog.Builder(activity).setTitle("알림")
        builder.setMessage(dialog)
        builder.setCancelable(false)

        builder.setPositiveButton("네") { _, _ ->

            val dispatcher = this.onBackPressedDispatcher
            dispatcher.addCallback(this) {
                //Toast.makeText(activity, "이전 화면으로 돌아갈게요.", Toast.LENGTH_LONG).show()
                finish()
            }
            dispatcher.onBackPressed()
        }

        builder.setNegativeButton("아니요") { _, _ -> }
        builder.show()
    }
}