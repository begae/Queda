package com.whoasys.queda

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.navigation.findNavController
import com.whoasys.queda.databinding.ActivityJoinBinding
import com.whoasys.queda.entities.User
import com.whoasys.queda.entities.UserService

class JoinActivity : AppCompatActivity() {

    private var idFromLogin: String? = null
    private var pwFromLogin: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val b = ActivityJoinBinding.inflate(layoutInflater)
        var loggedIn: User? = null

        b.id.setText(idFromLogin)
        b.pw.setText(pwFromLogin)

        b.cancel.setOnClickListener {

            //popUp(this, "회원가입을 취소하시겠어요?", exit(), )
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
                        putString("saved_id", loggedIn!!.id)
                        putString("saved_name", loggedIn!!.name)
                        putBoolean("saved_is_manager", false)
                        putInt("saved_store", 0)
                        apply()
                    }

                    //this.findNavController().navigate(R.id.action_join_to_editKeyword)

                } else {

                    b.joinReal.isSelected = false
                    Toast.makeText(this, "동일한 아이디가 존재해요.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}