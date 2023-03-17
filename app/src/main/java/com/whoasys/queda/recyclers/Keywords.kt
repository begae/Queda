package com.whoasys.queda.recyclers

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
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

        val layout = inflater.inflate(R.layout.activity_keyword, container, false)
        val view = layout.findViewById<RecyclerView>(R.id.keywords_list_view)

        with(view) {
            layoutManager = GridLayoutManager(context, 3)
            adapter = KeywordsAdapter(keywordsList?: emptyList())
        }
        return view
    }
}