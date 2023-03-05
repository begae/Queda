package com.whoasys.queda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.whoasys.queda.databinding.FragmentFeedBinding
import com.whoasys.queda.entities.Post
import com.whoasys.queda.entities.Store
import com.whoasys.queda.entities.User

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rv_feed = binding.rvFeed
        val itemList = ArrayList<Post>()


        //~itemList.add(...)로 아이템 추가~
        //거리조건, 최신순 정렬은 쿼리문으로 처리하고, 그 결과를 <Post>리스트로 담아 순차 추가하면 편할듯함


        //확인용 엉터리데이터------------------
        val UserList = ArrayList<User>()
        val StoreList = ArrayList<Store>()
        val PostList = ArrayList<Post>()
        UserList.add(User("1","1","1","1",false))

        StoreList.add(Store("testStore11","", UserList[0],"testLocation11",1.0,
            1.0,"123", "",null,null,null,false))
        StoreList.add(Store("testStore22","", UserList[0],"testLocation22",1.0,
            1.0,"123", "",null,null,null,false))

        UserList.add(User("1","1","1","1",true, StoreList[0]))
        UserList.add(User("2","2","2","2",true, StoreList[1]))

        PostList.add(Post("1번 포스트 제목",UserList[1],"",false,"","",
            "",1,null))
        PostList.add(Post("2번 포스트 제목",UserList[2],"",false,"","",
            "",2,null))
        PostList.add(Post("3번 포스트 제목",UserList[2],"",false,"","",
            "",3,null))

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() {
            TODO("Not yet implemented")
        }
    }
}