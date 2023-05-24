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
            "stained", "stained", "글라스", "stained@whoasys.com",
            35.855687, 128.492680
        )

        val sampleUser2 = User(
            "hygge", "hygge", "휘게", "hygge@whoasys.com",
            35.849768, 128.484002
        )

        val sampleUser3 = User(
            "garden", "garden", "상대", "garden@whoasys.com",
            35.859104, 128.492707
        )

        val sampleUser4 = User(
            "jieun", "jieun", "이지은", "lje@whoasys.com",
            35.856033, 128.496993
        )

        val sampleUser5 = User(
            "best", "best", "베스트", "best@whoasys.com",
            35.852899, 128.495383
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

        val library = Store("스테인드 글라스", "689-65-00286",
            "대구 달서구 서당로9길 51 2층", 35.855867, 128.492680,
            "010-3146-1149", "12:00 - 22:00")

        val engineeringDept = Store("HYGGE 휘게", "503-23-40363",
            "대구 달서구 달서대로109안길 60", 35.849768, 128.484002,
            "0507-1472-7790", "09:30 - 24:00")

        val naturalScienceDept = Store("상대가든", "111-04-01137",
            "대구 달서구 선원로 4", 35.85370115, 128.48243527,
            "053-582-0030", "10:00 - 22:00")

        val mainStadium = Store("이지은 뷰티샵", "501-77-00223",
            "대구 달서구 계대동문로9길 9", 35.856033, 128.496993,
            "010-6809-0315", "10:30 - 20:00")

        val adminStore = Store("베스트 잉크", "000-00-00000",
            "대구 달서구 서당로 13", 35.852899, 128.495383, "053-591-9511",
            "13:00 - 19:00")

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