package com.whoasys.queda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.whoasys.queda.databinding.StoreProfileBinding
import com.whoasys.queda.entities.NetworkService
import com.whoasys.queda.entities.Store

class StoreProfile : Fragment() {

    private var storeId: Int = 5
    private var store: Store? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            storeId = it.getInt("store_id")
        }

        val networkThread = Thread {
            store = NetworkService.call().getOneStore(storeId.toString()).execute().body()
        }

        networkThread.start()
        networkThread.join()
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

    companion object {

        @JvmStatic
        fun newInstance(storeId: Int) =
            StoreProfile().apply {
                arguments = Bundle().apply {
                    putInt("store_id", storeId)
                }
            }
    }
}