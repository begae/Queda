package com.whoasys.queda.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whoasys.queda.databinding.MapBinding
//import net.daum.mf.map.api.MapView

class Map : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val b = MapBinding.inflate(inflater, container, false)

        return b.root
    }
}