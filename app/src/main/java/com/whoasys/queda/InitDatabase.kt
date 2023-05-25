package com.whoasys.queda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whoasys.queda.databinding.InitBinding
import com.whoasys.queda.entities.*


// DB에 데이터가 하나도 없을때 본 프래그먼트를 실행하면 최소한의 데이터를 생성합니다.
// 회원가입: 5명 가입시킴. 또 가입하면 아이디 중복으로 인해 Null을 받게 되고 앱 팅김.
// 매장등록: 5개 등록시킴. 또 등록하면 계속 등록됨.
// 관리자등록: 회원 5명에 대하여 관리자로 승격(isManager=true) 하고 매장id와 연결함
// 오류안내페이지 생성: PostId를 1,2,3으로 가지는 오류안내 게시물 생성. 예외처리용 데이터. 계속 누르면 계속 생성됨.

class InitDatabase : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val b = InitBinding.inflate(inflater, container, false)

        val sampleUser1 = User(
            "solbit", "solbit", "솔빛", "solbit@whoasys.com",
            35.851605, 128.482332
        )

        val sampleUser2 = User(
            "mukka", "mukka", "무까", "mukka@whoasys.com",
            35.852310, 128.494845
        )

        val sampleUser3 = User(
            "seeik", "seeik", "씨익", "seeik@whoasys.com",
            35.860338, 128.497160
        )

        val sampleUser4 = User(
            "seomoon", "seomoon", "서문", "seomoon@whoasys.com",
            35.858746, 128.493906
        )

        val sampleUser5 = User(
            "subpharm", "subpharm", "지하철", "subpharm@whoasys.com",
            35.858434, 128.492825
        )

        val team = arrayOf(sampleUser1, sampleUser2, sampleUser3, sampleUser4, sampleUser5)

        lateinit var networkThread: Thread
        var name: String?
        var names = emptyArray<String?>()

        b.initJoin.setOnClickListener {

            for (member in team) {
                networkThread = Thread {
                    name = UserService.call().join(member).execute().body()?.name
                    print(name)
                    names = names.plus(name)
                }
                networkThread.start()
                networkThread.join()
            }

            print(" 저장됨!\n")

        }

        val library = Store("솔빛헤어", "292-20-00382",
            "대구 달서구 호산동로35북길 93", 35.851605, 128.482332,
            "053-263-0101", "10:00 - 20:30")

        val engineeringDept = Store("무까식당", "182-02-01571",
            "대구 달서구 호산동로35북길 95", 35.851487, 128.482310,
            "053-592-6663", "10:30 - 21:00")

        val naturalScienceDept = Store("씨익", "111-04-01137",
            "대구 달서구 호산동로35북길 99 1층", 35.851276, 128.482319,
            "0507-1390-1363", "11:00 - 21:00")

        val mainStadium = Store("서문문화사", "483-11-00481",
            "대구 달서구 호산동로35길 14-18", 35.851531, 128.485101,
            "053-554-6622", "10:00 - 20:00")

        val adminStore = Store("계명지하철약국", "106-28-24056",
            "대구 달서구 달구벌대로 1014", 35.853132, 128.478077,
            "053-554-6622",
            "08:40 - 19:00")

        val school = arrayOf(library, engineeringDept, naturalScienceDept, mainStadium, adminStore)
        var storeId: Int
        var storeIds = emptyArray<Int>()

        b.initRegister.setOnClickListener {

            for (facility in school) {
                networkThread = Thread {
                    storeId = StoreService.call().register(facility).execute().body()?: 0
                    print(storeId)
                    storeIds = storeIds.plus(storeId)
                }
                networkThread.start()
                networkThread.join()
            }

            print(" 저장됨!\n")

        }

        var response: Boolean
        var responses = emptyArray<Boolean>()

        b.initUpdate.setOnClickListener {

            for (i: Int in team.indices) {
                networkThread = Thread {
                    response = UserService.call().updateUserAsManager(team[i].id, storeIds[i]).execute().body()?: false
                    print(response)
                    responses = responses.plus(response)
                }
                networkThread.start()
                networkThread.join()
            }

            print("<- 업데이트 결과\n")

        }

        /*val postOne = Post("오류가 발생했삼", administrator, "관리자에게 문의하삼",
            false, null, null, null, null, null)

        val postTwo = Post("오류가 발생했삼", administrator, "관리자에게 문의하삼",
            false, null, null, null, null, null)

        val postThree = Post("오류가 발생했삼", administrator, "관리자에게 문의하삼",
            false, null, null, null, null, null)

        val errorPosts = arrayOf(postOne, postTwo, postThree)
        var postId: Int

        b.initPosts.setOnClickListener {

            for (post in errorPosts) {
                networkThread = Thread {
                    postId = PostService.call().savePost(post).execute().body()?: -3
                    print(postId)
                }
                networkThread.start()
                networkThread.join()
            }

            print(" 게시물 생성함!\n")

        }*/

        val listOfKeywords: List<String> = listOf("가구", "가전", "건강식품", "문구서적", "반려동물용품", "스포츠용품", "식료품", "신발", "악기", "악세사리",
        "약국", "완구취미", "유아용품", "음반DVD", "의류", "자동차", "철물점", "컴퓨터", "피트니스", "화장품")

        b.initKeyword.setOnClickListener {

            for (key in listOfKeywords) {

                val kw = Keyword(key)

                networkThread = Thread {
                    val id = KeywordService.call().saveKeyword(kw).execute().body()
                    print(kw.value + "as $id saved.\n")
                }
                networkThread.start()
                networkThread.join()
            }

        }

        return b.root
    }
}