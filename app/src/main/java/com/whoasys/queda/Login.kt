package com.whoasys.queda

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.whoasys.queda.databinding.LoginBinding
import com.whoasys.queda.entities.User
import com.whoasys.queda.entities.UserService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Login.newInstance] factory method to
 * create an instance of this fragment.
 */
class Login : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val b = LoginBinding.inflate(inflater, container, false)

        var loggedIn: User? = null

        b.joinFirst.setOnClickListener {

            val pair1 = Pair("entered_id", b.id.text.toString())
            val pair2 = Pair("entered_pw", b.pw.text.toString())
            val bundle = bundleOf(pair1, pair2)

            val intent = Intent(activity, JoinActivity::class.java).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, bundle)
            }
            startActivity(intent)
        }

        b.loginBtn.setOnClickListener {

            if (b.id.text.isEmpty() || b.pw.text.isEmpty()) {
                b.loginBtn.isSelected = false
                Toast.makeText(activity, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show()
            }
            else {

                val networkThread = Thread {
                    loggedIn = UserService.call().login(b.id.text.toString(),
                        b.pw.text.toString()).execute().body()
                }

                networkThread.start()
                networkThread.join()

                if (loggedIn != null) {

                    Toast.makeText(activity, "환영합니다, " +
                            "${loggedIn!!.name} 님.", Toast.LENGTH_LONG).show()

                    val pair0 = Pair("user_id", loggedIn!!.id)
                    val bundle = bundleOf(pair0)
                    /*view?.findNavController()
                        ?.navigate(R.id.action_login_to_feed, bundle)*/
                }

                else {
                    b.loginBtn.isSelected = false
                    Toast.makeText(activity, "일치하는 정보가 없어요.", Toast.LENGTH_LONG).show()
                }
            }
        }

        return b.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Login.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Login().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}