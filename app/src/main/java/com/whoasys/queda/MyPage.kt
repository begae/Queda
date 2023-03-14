package com.whoasys.queda

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.whoasys.queda.databinding.MyPageBinding

class MyPage : Fragment() {

    private var _binding: MyPageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = MyPageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val myStore = getView()?.findViewById<Button>(R.id.myStore)
//
//        myStore?.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//                .setTitle("알림")
//            builder.setMessage("               등록된 내 매장이 없습니다.\n                        등록하시겠습니까?")
//            builder.setCancelable(false)
//            fun skipPage() {
//                val intent = Intent(getActivity(), StoreRegister::class.java)
//                startActivity(intent)
//            }
//
//            builder.setPositiveButton("확인", object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface, which: Int) {
//                    when(which){
//                        DialogInterface.BUTTON_POSITIVE ->
//                            skipPage()
//                    }
//                }
//            })
//            builder.setNegativeButton("뒤로", object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface, which: Int) {
//                }
//            })
//            // 다이얼로그를 띄워주기
//            builder.show()
//        }
//
//        val signOut = getView()?.findViewById<Button>(R.id.signOut)
//        signOut?.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//                .setTitle("알림")
//            builder.setMessage("로그아웃 하시겠습니까?")
//            builder.setCancelable(false)
//            fun skipPage() {
//                val intent = Intent(getActivity(), StoreRegister::class.java)
//                startActivity(intent)
//            }
//
//            builder.setPositiveButton("확인", object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface, which: Int) {
//                    when(which){
//                        DialogInterface.BUTTON_POSITIVE ->
//                            skipPage()
//                    }
//                }
//            })
//            builder.setNegativeButton("뒤로", object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface, which: Int) {
//                }
//            })
//            // 다이얼로그를 띄워주기
//            builder.show()
//        }
//
//    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myStore = getView()?.findViewById<Button>(R.id.myStore)

        myStore?.setOnClickListener {
                val intent = Intent(getActivity(), StoreRegister::class.java)
                startActivity(intent)
        }

        val signOut = getView()?.findViewById<Button>(R.id.signOut)
        signOut?.setOnClickListener {
                val intent = Intent(getActivity(), StoreRegister::class.java)
                startActivity(intent)
        }

    }

}