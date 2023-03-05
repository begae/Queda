package com.whoasys.queda

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.whoasys.queda.databinding.FragmentJoinBinding

class JoinFragment : Fragment() {

    private var idFromLogin: String? = null
    private var pwFromLogin: String? = null
    var loggedIn: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idFromLogin = it.getString("id")
            pwFromLogin = it.getString("pw")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val b = FragmentJoinBinding.inflate(inflater, container, false)

        b.id.setText(idFromLogin)
        b.pw.setText(pwFromLogin)

        b.joinReal.setOnClickListener {

            if (b.id.text.isEmpty() || b.pw.text.isEmpty()
                || b.name.text.isEmpty() || b.email.text.isEmpty()) {

                b.joinReal.isSelected = false
                Toast.makeText(activity, "필수 정보를 전부 입력해주세요.", Toast.LENGTH_LONG).show()
            }

            else if (!b.personalInfoAgree.isChecked) {

                b.joinReal.isSelected = false
                Toast.makeText(activity, "약관에 동의해 주세요.", Toast.LENGTH_LONG).show()
            }

            else {

                val networkThread = Thread {

                    val new = User(b.id.text.toString(), b.pw.text.toString(),
                                b.name.text.toString(), b.email.text.toString(),
                                null, null)
                    loggedIn = NetworkService.call().join(new).execute().body()
                }

                networkThread.start()
                networkThread.join()

                if (loggedIn != null) {

                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                    sharedPref?.edit {
                        putString("saved_id", loggedIn!!.id)
                        putString("saved_name", loggedIn!!.name)
                        putBoolean("saved_is_manager", false)
                        putInt("saved_store", 0)
                        apply()
                    }

                    view?.findNavController()?.navigate(R.id.action_joinFragment_to_firstKeywordFragment)

                } else {

                    b.joinReal.isSelected = false
                    Toast.makeText(activity, "동일한 아이디가 존재해요.", Toast.LENGTH_LONG).show()
                }
            }
        }

        return b.root
    }

    companion object {

        @JvmStatic
        fun newInstance(idFromLogin: String, pwFromLogin: String) =
            JoinFragment().apply {
                arguments = Bundle().apply {
                    putString("id", idFromLogin)
                    putString("pw", pwFromLogin)
                }
            }
    }
}