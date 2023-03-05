package com.whoasys.queda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.whoasys.queda.databinding.FragmentRegisterBinding
import com.whoasys.queda.entities.Store
import com.whoasys.queda.entities.User

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val b = FragmentRegisterBinding.inflate(layoutInflater)
        var response: Int? = null

        var fakeUser: User? = User(
            "ksr",
            "ksr", "김세령",
            "ksr@whoasys.com"
        )

        var networkThread = Thread {
            fakeUser = NetworkService.call().join(fakeUser!!).execute().body()
        }

        networkThread.start()
        networkThread.join()

        if (fakeUser == null) {
            Toast.makeText(activity, "User save failed.", Toast.LENGTH_LONG).show()

        } else {
            Toast.makeText(activity, "User name: ${fakeUser!!.name} Saved.", Toast.LENGTH_LONG).show()

        }


        b.register.setOnClickListener {

            val store = Store("김세령 집",
                "012-34-78901", fakeUser!!,
                "대구 수성구 지산로14길 83",
                35.826551, 128.638423, "010-2789-3764",
                "12:00 - 08:00")

            networkThread = Thread {
                response = NetworkService.call().register(store).execute().body()
            }

            networkThread.start()
            networkThread.join()
            Toast.makeText(activity, "Store id: $response saved.", Toast.LENGTH_LONG).show()
        }

        b.updateUserStore.setOnClickListener {

            networkThread = Thread {
                fakeUser = NetworkService.call()
                    .updateUserAsManager("ksr", response!!).execute().body()
            }

            networkThread.start()
            networkThread.join()
            Toast.makeText(activity, "User id: ksr updated.", Toast.LENGTH_LONG).show()
        }

        return b.root
    }
}