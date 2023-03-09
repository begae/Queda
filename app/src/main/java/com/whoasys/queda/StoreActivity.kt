package com.whoasys.queda

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.whoasys.queda.databinding.ActivityStoreBinding
import com.whoasys.queda.entities.Store
import com.whoasys.queda.entities.User
import com.whoasys.queda.entities.UserService

class StoreActivity : AppCompatActivity() {

    private var storeId: Int = 1
    private var manager: User? = null
    private var store: Store? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var networkThread = Thread {
            store = UserService.call().getOneStore(storeId).execute().body()
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
    }
}