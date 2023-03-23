package com.whoasys.queda.recyclers

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.whoasys.queda.R
import com.whoasys.queda.entities.Keyword
import com.whoasys.queda.entities.KeywordService

class Keywords : Fragment() {

    private var keywordsList: List<Keyword>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val networkThread = Thread {
            keywordsList = KeywordService.call().getAllKeywords().execute().body()
        }

        networkThread.start()
        networkThread.join()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.keyword_item_list, container, false)

        if (view is RecyclerView) {
            with(view) {
                layoutManager = GridLayoutManager(context, 3)
                adapter = KeywordsAdapter(keywordsList?: emptyList())
            }
        }
        return view
    }
}