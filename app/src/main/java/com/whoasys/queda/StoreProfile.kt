package com.whoasys.queda

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import com.whoasys.queda.databinding.StoreProfileBinding
import com.whoasys.queda.entities.Store
import com.whoasys.queda.entities.StoreService
import com.whoasys.queda.entities.User
import com.whoasys.queda.entities.UserService

class StoreProfile : Fragment() {

    private var storeId: Int = 1
    private var manager: User? = null
    private var store: Store? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            storeId = it.getInt("store_id")
        }

        var networkThread = Thread {
            store = StoreService.call().getOneStore(storeId).execute().body()
        }

        networkThread.start()
        networkThread.join()

        networkThread = Thread {
            manager = UserService.call().findUserByStoreId(storeId).execute().body()
        }

        networkThread.start()
        networkThread.join()

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        sharedPref?.edit {
            putString("manager_id", manager?.id)
            apply()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val b = StoreProfileBinding.inflate(layoutInflater)

        b.storeName.text = store!!.name
        b.storeAddress.text = store!!.address
        b.storePhone.text = store!!.phone
        b.storeOpenTime.text = store!!.openTime

        return b.root
    }
}