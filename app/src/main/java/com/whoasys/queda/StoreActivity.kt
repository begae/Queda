package com.whoasys.queda

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.whoasys.queda.databinding.ActivityStoreBinding
import com.whoasys.queda.entities.Store
import com.whoasys.queda.entities.StoreService
import com.whoasys.queda.entities.User
import com.whoasys.queda.entities.UserService

class StoreActivity : AppCompatActivity() {

    private var storeId: Int = 1
    private var manager: User? = null
    private var store: Store? = null

    private lateinit var intent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        var networkThread = Thread {
            store = StoreService.call().getOneStore(storeId).execute().body()
        }

        networkThread.start()
        networkThread.join()

        storeId = store?.id ?: 1

        networkThread = Thread {
            manager = UserService.call().findUserByStoreId(storeId).execute().body()
        }

        networkThread.start()
        networkThread.join()

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        sharedPref?.edit {
            putString("manager_id", manager?.id)
            apply()
        }

        val b = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(b.root)

        val navHostFragment = b.storeNavContainer.getFragment<Fragment>()
        val navController = navHostFragment.findNavController()
        b.storeNavBar.setupWithNavController(navController)

        b.storeName.text = store!!.name
        b.storeAddress.text = store!!.address
        b.storePhone.text = store!!.phone
        b.storeOpenTime.text = store!!.openTime

        fun onOptionsItemSelected(item: MenuItem): Boolean {


            return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }

        val write_post_btn = findViewById<Button>(R.id.write_post_btn)
        write_post_btn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("알림")
            builder.setMessage("새 글을 작성하시겠습니까?")
            builder.setCancelable(false)
            builder.setPositiveButton("확인") { dialog, which ->
                val intent = Intent(this, KeywordActivity::class.java)
                startActivity(intent)
            }
            builder.setNegativeButton("뒤로") { dialog, which ->

            }
            builder.show()
        }

        val edit_info_btn = findViewById<Button>(R.id.edit_info_btn)
        edit_info_btn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("알림")
            builder.setMessage("정보 수정을 하시겠습니까?")
            builder.setCancelable(false)
            builder.setPositiveButton("확인") { dialog, which ->
                val intent = Intent(this, StoreModifying::class.java)
                startActivity(intent)
            }
            builder.setNegativeButton("뒤로") { dialog, which ->

            }
            builder.show()
        }

        var i = true
        val follow_btn = findViewById<ImageButton>(R.id.follow_btn)
        follow_btn.setOnClickListener {
            if(i){
                follow_btn.setImageResource(R.drawable.h2)
                i = false
            }
            else{
                follow_btn.setImageResource(R.drawable.h1)
                i = true
            }
        }


    }
}