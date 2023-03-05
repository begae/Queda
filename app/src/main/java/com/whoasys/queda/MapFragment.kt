package com.whoasys.queda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whoasys.queda.databinding.FragmentMapBinding
import net.daum.mf.map.api.MapView

class MapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val b = FragmentMapBinding.inflate(inflater, container, false)

        return b.root
    }
}