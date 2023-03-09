package com.whoasys.queda.etc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whoasys.queda.databinding.InitBinding
import com.whoasys.queda.entities.*

class Init : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val b = InitBinding.inflate(inflater, container, false)

        val begae = User(
            "begae", "begae", "김세령", "ksr@whoasys.com",
            35.82655571, 128.63841783
        )

        val kimsmj = User(
            "kimsmj", "kimsmj", "김서연", "ksy@whoasys.com",
            35.83151431, 128.54574722
        )

        val hagaji = User(
            "hagaji", "hagaji", "하남규", "hng@whoasys.com",
            35.83334861, 128.529161023
        )

        val acctract = User(
            "acctract", "acctract", "김한솔", "khs@whoasys.com",
            35.85520714, 128.49362696
        )

        val administrator = User("admin", "admin", "admin","admin@whoasys.com",
            0.0, 0.0)

        val team = arrayOf(begae, kimsmj, hagaji, acctract, administrator)

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

        val library = Store("계명대학교 성서캠퍼스 동산도서관", "012-34-78901",
            "대구 달서구 달구벌대로 1095", 35.856418, 128.487185,
            "053-580-5682", "08:00 - 23:00")

        val engineeringDept = Store("계명대학교 성서캠퍼스 공학관", "123-45-67890",
            "대구 달서구 달서대로 675", 35.85933090, 128.48701244,
            "053-580-5262", "09:00 - 18:00")

        val naturalScienceDept = Store("계명대학교 성서캠퍼스 백은관", "012-34-56789",
            "대구 달서구 달구벌대로 1095", 35.85370115, 128.48243527,
            "053-580-5031", "09:00 - 18:00")

        val mainStadium = Store("계명대학교 성서캠퍼스 대운동장", "234-56-78901",
            "대구 달서구 달구벌대로 1095", 35.85267052, 128.48889710,
            "053-580-6210", "09:00 - 18:00")

        val adminStore = Store("오류가 발생했삼", "000-00-00000",
            "대구 모처", 0.0, 0.0, "000-000-0000",
            "00:00 - 00:00")

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

        val postOne = Post("오류가 발생했삼", administrator, "관리자에게 문의하삼",
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

        }

        return b.root
    }
}