package com.whoasys.queda.recyclers

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.whoasys.queda.R
import com.whoasys.queda.entities.Post
import com.whoasys.queda.entities.Store
import com.whoasys.queda.entities.User
import com.whoasys.queda.databinding.FeedListBinding

/**
 * A fragment representing a list of Items.
 */
class Feed : Fragment() {

    private var columnCount = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    private var _binding: FeedListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*
        val view = inflater.inflate(R.layout.feed_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = FeedAdapter(PlaceholderContent.ITEMS)
            }
        }
        return view
         */

        _binding = FeedListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rv_feed = binding.feedList
        val itemList = ArrayList<Post>()


        //~itemList.add(...)로 아이템 추가~
        //거리조건, 최신순 정렬은 쿼리문으로 처리하고, 그 결과를 <Post>리스트로 담아 순차 추가하면 편할듯함


        //확인용 더미데이터------------------
        val UserList = ArrayList<User>()
        val StoreList = ArrayList<Store>()
        val PostList = ArrayList<Post>()

        StoreList.add(
            Store("testStore11","","testLocation11",1.0,
                1.0,"123", "",null,null,null,false)
        )
        StoreList.add(
            Store("testStore22","","testLocation22",1.0,
                1.0,"123", "",null,null,null,false)
        )

        UserList.add(User("1","1","1","1",1.0, 1.0, true, StoreList[0]))
        UserList.add(User("2","2","2","2",1.0, 1.0, true, StoreList[1]))

        /*(PostList.add(
            Post("1번 포스트 제목",UserList[0],"",false,"","",
            null, null, null)
        )
        PostList.add(
            Post("2번 포스트 제목",UserList[1],"",false,"","",
            "",2,null)
        )
        PostList.add(
            Post("3번 포스트 제목",UserList[1],"",false,"","",
            "",3,null)
        )*/

        itemList.add(PostList[0])
        itemList.add(PostList[1])
        itemList.add(PostList[2])
        //-------------------


        val feedAdapter = FeedAdapter(itemList)
        feedAdapter.notifyDataSetChanged()

        rv_feed.adapter = feedAdapter

        feedAdapter.itemClickListener = object : FeedAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val id = itemList[position].id
                //id를 받아 게시물 상세페이지로 넘어갈 수 있게끔 핸들
                //아래는 클릭테스트
                Toast.makeText(getActivity(), "${id} 클릭함", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            Feed().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}