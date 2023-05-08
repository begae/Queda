package com.whoasys.queda

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
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
    private lateinit var intent: Intent

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

        b.myPageEditKeyword.setOnClickListener {

            intent = Intent(requireContext(), KeywordActivity::class.java)
            jumpTo(intent, "관심 키워드를 수정하시겠어요?")
        }

        b.myPageMyStore.setOnClickListener {

            if (isManager) {
                view?.findNavController()
                    ?.navigate(R.id.action_myPage_to_storeActivity, bundle)
            }
            else {
                intent = Intent(requireContext(), RegisterActivity::class.java)
                jumpTo(intent,"매장이 등록되어있지 않아요.\n등록하시겠어요?")
            }
        }

        b.myPageLogout.setOnClickListener {
            val builder= androidx.appcompat.app.AlertDialog.Builder(requireContext())
            builder.setTitle("알림")
            builder.setMessage("로그아웃 하시겠습니까?")
            builder.setCancelable(false)
            builder.setPositiveButton("확인", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    view?.findNavController()
                        ?.navigate(R.id.action_myPage_to_login, bundle)
                }
            })
            builder.setNegativeButton("뒤로", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {}
            })
            builder.show()

//            if (userId != "admin") {
//
//                intent = Intent(requireContext(), LoginActivity::class.java)
//                jumpTo(intent, "로그아웃 하시겠어요?")

//                sharedPref?.edit {
//                    putString("user_id", "admin")
//                    putString("user_name", "admin")
//                    putBoolean("user_is_manager", false)
//                    putInt("user_store", 5)
//                    apply()
//                }
            }

        return b.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

    private fun jumpTo(intent: Intent, dialog: String) {

        val builder = AlertDialog.Builder(requireContext()).setTitle("알림")
        builder.setMessage(dialog)
        builder.setCancelable(false)

        builder.setPositiveButton("네") { _, _ ->
            startActivity(intent)
        }

        builder.setNegativeButton("아니요") { _, _ -> }
        builder.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("정말로 종료하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("네") { dialog, id -> requireActivity().onBackPressed() }
                    .setNegativeButton("아니요", null)

                val alert = builder.create()
                alert.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}