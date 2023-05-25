package com.whoasys.queda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
//import com.bumptech.glide.Glide
import com.whoasys.queda.databinding.ActivityStoreModifyingBinding

class StoreModifying : AppCompatActivity() {

    private lateinit var mEtAdress : EditText

    lateinit var binding: ActivityStoreModifyingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_modifying)

        //갤러리 이미지 불러오기
        binding = ActivityStoreModifyingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val galleryBtn = findViewById<Button>(R.id.galleryBtn)
        //val imageView = findViewById<ImageView>(R.id.imageView)

        //버튼 이벤트
        binding.galleryBtn.setOnClickListener {

            //갤러리 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }


        //주소
        mEtAdress = findViewById(R.id.storeAddress)
        mEtAdress.setOnClickListener {
            //주소 검색 웹뷰로 이동
            val intent = Intent(this, SearchActivity::class.java)
            getSearchResult.launch(intent)
        }

        //확인버튼
        val SaveBtn = findViewById<Button>(R.id.SaveBtn)
        SaveBtn.setOnClickListener {
            val intent = Intent(this, StoreActivity::class.java)
            startActivity(intent)
        }

        //뒤로가기버튼
        val BackBtn = findViewById<Button>(R.id.BackBtn)
        BackBtn.setOnClickListener {
            val intent = Intent(this, StoreActivity::class.java)
            startActivity(intent)
        }
    }

    //주소 결과 가져오기
    private val getSearchResult : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        //serach activtity 로부터의 결과 값이 이곳으로 전달된다.
        if(it.resultCode == RESULT_OK){
            if(it.data != null){
                val data = it.data!!.getStringExtra("data")
                mEtAdress.setText(data)
            }
        }
    }


    //이미지 결과 가져오기
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {

        //결과 코드 OK , 결가값 null 아니면
        if (it.resultCode == RESULT_OK && it.data != null) {
            //값 담기
            val uri = it.data!!.data

            /*화면에 보여주기
            Glide.with(this)
                .load(uri) //이미지
                .into(binding.imageView) */
        }
    }

}