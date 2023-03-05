package com.whoasys.queda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whoasys.queda.databinding.FragmentStoreProfileBinding

class StoreProfileFragment : Fragment() {

    private var storeId: Int? = null
    private var store: Store? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            storeId = it.getInt("store_id")
        }

        val networkThread = Thread {
            store = NetworkService.call().getOneStore(storeId!!.toString()).execute().body()
        }

        networkThread.start()
        networkThread.join()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val b = FragmentStoreProfileBinding.inflate(inflater, container, false)

        if (store!= null) {
            b.storeName.text = store!!.name
            b.storeAddr.text = store!!.address
            b.storePhone.text = store!!.phone
            b.storeOpenTime.text = store!!.openTime
        }

        return b.root
    }

    companion object {

        @JvmStatic
        fun newInstance(storeId: Int) =
            StoreProfileFragment().apply {
                arguments = Bundle().apply {
                    putInt("store_id", storeId)
                }
            }
    }
}