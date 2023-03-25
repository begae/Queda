package com.whoasys.queda

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.os.bundleOf
import com.whoasys.queda.databinding.ActivityLoginBinding
import com.whoasys.queda.entities.User
import com.whoasys.queda.entities.UserService

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        var loggedIn: User? = null

        b.joinFirst.setOnClickListener {

            val pair1 = Pair("entered_id", b.id.text.toString())
            val pair2 = Pair("entered_pw", b.pw.text.toString())
            val bundle = bundleOf(pair1, pair2)

            val intent = Intent(this, JoinActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, bundle)
            }
            startActivity(intent)
        }

        b.loginBtn.setOnClickListener {

            if (b.id.text.isEmpty() || b.pw.text.isEmpty()) {
                b.loginBtn.isSelected = false
                Toast.makeText(this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show()
            }
            else {

                val networkThread = Thread {
                    loggedIn = UserService.call().login(b.id.text.toString(),
                        b.pw.text.toString()).execute().body()
                }

                networkThread.start()
                networkThread.join()

                if (loggedIn != null) {

                    val store = loggedIn!!.store
                    val storeId = if (store != null) store.id else 0

                    val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
                    sharedPref?.edit {
                        putString("user_id", loggedIn!!.id)
                        putString("user_name", loggedIn!!.name)
                        putBoolean("user_is_manager", loggedIn!!.isManager)
                        putInt("user_store", storeId!!)
                        apply()
                    }

                    Toast.makeText(this, "환영합니다, " +
                            "${loggedIn!!.name} 님.", Toast.LENGTH_LONG).show()

                    // 실시간 현위치 데이터 넘겨야하는데 일단 회원가입때 등록된 데이터로 구현함
                    val pair0 = Pair("user_id", loggedIn!!.id)
                    val pair1 = Pair("user_latitude", loggedIn!!.latitude)
                    val pair2 = Pair("user_longitude", loggedIn!!.longitude)
                    val bundle = bundleOf(pair0, pair1, pair2)
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra(EXTRA_MESSAGE, bundle)
                    }
                    startActivity(intent)
                }

                else {
                    b.loginBtn.isSelected = false
                    Toast.makeText(this, "일치하는 정보가 없어요.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}