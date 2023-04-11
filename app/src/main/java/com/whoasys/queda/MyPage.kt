package com.whoasys.queda

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.whoasys.queda.databinding.MyPageBinding

class MyPage : Fragment() {

    private var _b: MyPageBinding? = null
    private val b get() = _b!!

    private var userId: String? = null
    private var isManager: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _b = MyPageBinding.inflate(inflater, container, false)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        sharedPref?.getString("user_id", userId)?: "admin"
        sharedPref?.getBoolean("user_is_manager", isManager)
        val pair = Pair("user_id", userId)
        val bundle = bundleOf(pair)


        b.myPageScraps.setOnClickListener {

            view?.findNavController()
                ?.navigate(R.id.action_myPage_to_scraps, bundle)
        }

        b.myPageFollowing.setOnClickListener {

            view?.findNavController()
                ?.navigate(R.id.action_myPage_to_following, bundle)
        }

        /*b.myPageEditKeyword.setOnClickListener {

            view?.findNavController()
                ?.navigate(R.id.action_myPage_to_editKeyword, bundle)
        }*/

        b.myPageMyStore.setOnClickListener {

            if (isManager) {
                view?.findNavController()
                    ?.navigate(R.id.action_myPage_to_storeProfile, bundle)
            }
            else {
                //popUp("등록된 내 매장이 없습니다.\n등록하시겠습니까?", R.id.action_myPage_to_register)
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("알림")
                builder.setMessage("등록된 내 매장이 없습니다.\n등록하시겠습니까?")
                builder.setCancelable(false)

                fun skipPage() {
                    val intent = Intent(requireContext(), StoreRegister::class.java)
                    startActivity(intent)
                }

                builder.setPositiveButton("확인", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        when(which){
                            DialogInterface.BUTTON_POSITIVE -> skipPage()
                        }
                    }
                })

                builder.setNegativeButton("뒤로", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                    }
                })
                builder.show()
            }
        }

        b.myPageLogout.setOnClickListener {

            if (userId != "admin") {

                //popUp("로그아웃 하시겠습니까?", 0)


                sharedPref?.edit {
                    putString("user_id", "admin")
                    putString("user_name", "admin")
                    putBoolean("user_is_manager", false)
                    putInt("user_store", 5)
                    apply()
                }
            }
        }

        return b.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}