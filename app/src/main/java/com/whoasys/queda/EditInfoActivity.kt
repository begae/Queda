package com.whoasys.queda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.whoasys.queda.databinding.ActivityEditInfoBinding

class EditInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityEditInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}