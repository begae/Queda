package com.whoasys.queda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.whoasys.queda.databinding.ActivityAddressBinding

class AddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}