package com.whoasys.queda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.whoasys.queda.databinding.RegisterBinding
import com.whoasys.queda.entities.Store
import com.whoasys.queda.entities.StoreService
import com.whoasys.queda.entities.UserService

class RegisterActivity : AppCompatActivity() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val b = RegisterBinding.inflate(layoutInflater)
        var response: Int? = null
        lateinit var networkThread: Thread


        b.registerBtn.setOnClickListener {

            val store = Store("계명대학교 성서캠퍼스 동산도서관",
                "012-34-78901",
                "대구 달서구 달구벌대로 1095",
                35.856418, 128.487185, "010-2789-3764",
                "12:00 - 08:00")

            networkThread = Thread {
                response = StoreService.call().register(store).execute().body()
            }

            networkThread.start()
            networkThread.join()
            Toast.makeText(activity, "Store id: $response saved.", Toast.LENGTH_LONG).show()
        }

        b.updateUserStore.setOnClickListener {

            var pass: Boolean? = false
            networkThread = Thread {
                pass = UserService.call()
                    .updateUserAsManager("fake", response!!).execute().body()
            }

            networkThread.start()
            networkThread.join()

            if (pass == true) {
                Toast.makeText(activity, "User id: ?? updated.", Toast.LENGTH_LONG)
                    .show()
            } else {
                Toast.makeText(activity, "User id: ?? update failed.", Toast.LENGTH_LONG)
                    .show()
            }
        }

        return b.root
    }
}