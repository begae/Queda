package com.whoasys.queda

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.whoasys.queda.databinding.LoginBinding
import com.whoasys.queda.entities.User
import com.whoasys.queda.entities.UserService

class Login : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var loggedIn: User? = null
        val b = LoginBinding.inflate(layoutInflater)

        b.joinFirst.setOnClickListener {

            val pair1 = Pair("entered_id", b.id.text.toString())
            val pair2 = Pair("entered_pw", b.pw.text.toString())
            val bundle = bundleOf(pair1, pair2)
            view?.findNavController()?.navigate(R.id.action_login_to_join, bundle)
        }

        b.loginBtn.setOnClickListener {

            if (b.id.text.isEmpty() || b.pw.text.isEmpty()) {
                b.loginBtn.isSelected = false
                Toast.makeText(activity, "아이디와 비밀번호를 다시 확인해주세요.", Toast.LENGTH_LONG).show()
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

                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                    sharedPref?.edit {
                        putString("saved_id", loggedIn!!.id)
                        putString("saved_name", loggedIn!!.name)
                        putBoolean("saved_is_manager", loggedIn!!.isManager)
                        putInt("saved_store", storeId!!)
                        apply()
                    }

                    Toast.makeText(activity, "환영합니다, " +
                            "${loggedIn!!.name} 님.", Toast.LENGTH_LONG).show()

                    val pair1 = Pair("user_latitude", loggedIn!!.latitude)
                    val pair2 = Pair("user_longitude", loggedIn!!.longitude)
                    val bundle = bundleOf(pair1, pair2)
                    view?.findNavController()
                        ?.navigate(R.id.action_login_to_feed, bundle)
                }

                else {
                    b.loginBtn.isSelected = false
                    Toast.makeText(activity, "일치하는 정보가 없어요.", Toast.LENGTH_LONG).show()
                }
            }
        }

        return b.root
    }
}