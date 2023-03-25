package com.whoasys.queda

import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.whoasys.queda.databinding.ActivityMainBinding
//import com.kakao.util.maps.helper.Utility.getKeyHash
//import net.daum.mf.map.api.MapView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.getBundleExtra(EXTRA_MESSAGE)
        val userId = bundle?.getString("user_id")

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = binding.mainNavContainer.getFragment<Fragment>()
        val navController = navHostFragment.findNavController()
        val bottomNavBar = binding.bottomNavBar
        bottomNavBar.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.feed, R.id.forYou,
                R.id.map, R.id.myPage
            ),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )

        fun onOptionsItemSelected(item: MenuItem): Boolean {
            return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }

        //val hashKey = getKeyHash(this)
        //println("해시키: $hashKey")
    }
}