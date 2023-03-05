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
import com.whoasys.queda.databinding.FragmentLoginBinding
import com.whoasys.queda.entities.User

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var loggedIn: User? = null
        val b = FragmentLoginBinding.inflate(layoutInflater)

        b.joinFirst.setOnClickListener {

            val pair1 = Pair("id", b.id.text.toString())
            val pair2 = Pair("pw", b.pw.text.toString())
            val bundle = bundleOf(pair1, pair2)
            view?.findNavController()?.navigate(R.id.action_loginFragment_to_joinFragment, bundle)
        }

        b.login.setOnClickListener {

            if (b.id.text.isEmpty() || b.pw.text.isEmpty()) {
                b.login.isSelected = false
                Toast.makeText(activity, "아이디와 비밀번호를 다시 확인해주세요.", Toast.LENGTH_LONG).show()
            }
            else {

                val networkThread = Thread {
                    loggedIn = NetworkService.call().login(b.id.text.toString(),
                        b.pw.text.toString()).execute().body()
                }

                networkThread.start()
                networkThread.join()

                if (loggedIn != null) {

                    val storeId = loggedIn!!.store!!.id?: 0
                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                    sharedPref?.edit {
                        putString("saved_id", loggedIn!!.id)
                        putString("saved_name", loggedIn!!.name)
                        putBoolean("saved_is_manager", loggedIn!!.isManager)
                        putInt("saved_store", storeId)
                        commit()
                    }

                    Toast.makeText(activity, "환영합니다, ${loggedIn!!.name} 님.", Toast.LENGTH_LONG).show()
                    view?.findNavController()?.navigate(R.id.action_loginFragment_to_navigation_feed)

                    val pair = Pair("id", loggedIn!!.id)
                    val bundle = bundleOf(pair)
                }

                else {
                    b.login.isSelected = false
                    Toast.makeText(activity, "일치하는 정보가 없어요.", Toast.LENGTH_LONG).show()
                }
            }
        }

        return b.root
    }
}