package com.whoasys.queda.recyclers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.whoasys.queda.Posting
import com.whoasys.queda.databinding.FeedPageBinding
import com.whoasys.queda.databinding.StoreProfileBinding
import com.whoasys.queda.entities.Store
import com.whoasys.queda.entities.StoreService
import com.whoasys.queda.entities.User
import com.whoasys.queda.entities.UserService

class FeedPage : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val b = FeedPageBinding.inflate(layoutInflater)

        return b.root
    }
    /*
    companion object {

        @JvmStatic
        fun newInstance(storeId: Int) =
            Posting().apply {
                arguments = Bundle().apply {
                    putInt("store_id", storeId)
                }
            }
    }
     */
}