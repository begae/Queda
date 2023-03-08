package com.whoasys.queda

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.whoasys.queda.databinding.ActivityStoreBinding
import com.whoasys.queda.entities.NetworkService
import com.whoasys.queda.entities.Store

class StoreActivity : AppCompatActivity() {

    private var storeId: Int = 5
    private var store: Store? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val networkThread = Thread {
            store = NetworkService.call().getOneStore(storeId.toString()).execute().body()
        }

        networkThread.start()
        networkThread.join()

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