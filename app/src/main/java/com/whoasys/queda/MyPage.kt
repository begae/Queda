package com.whoasys.queda

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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

        // 1. 각 페이지마다 sharedPref 에 접근해서 아이디를 얻어오기
        // 2. 중요한 페이지에서만 sharedPref (앱 내부에 저장된 유저 정보. 로그인/회원가입 때 기록됨) 에 접근하고
        // 나머지에서는 bundle 로 아이디만 사용하기
        // 3. ViewModel 이라는 거 사용하기
        // 라는 세가지 방법이 있습니다. 3번은 잘 모르고, 1~2번은 비슷합니다.
        // 대신 1번은 자동로그인 느낌이라 앱 껐다가 켜도 계속 있음 (추정)
        // 왜냐면 어차피 아이디만 가지고는 아무것도 못하고 서버에 아이디를 보내서 뭔가 객체를 받아와야하거든요.
        // sharedPref 에도 별로 중요한 데이터는 없습니다. Login 프래그먼트 보시면 그냥 기본적인 정보만 기록하고 있습니다.

        fun popUp(content: String, action: Int) {

            val builder = AlertDialog.Builder(activity).setTitle("알림")
            builder.setMessage(content)

            builder.setPositiveButton(
                "확인"
            ) { _, _ ->
                view?.findNavController()
                    ?.navigate(action, bundle)
            }

            builder.setNegativeButton(
                "취소"
            ) { _, _ -> }

            builder.show()
        }


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
            }
        }

        b.myPageLogout.setOnClickListener {

            if (userId != "admin") { //이거 뭔가 문제가 있다

                popUp("로그아웃 하시겠습니까?", 0)

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