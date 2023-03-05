package com.whoasys.queda

import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
//import com.kakao.util.maps.helper.Utility.getKeyHash
import com.whoasys.queda.databinding.ActivityMainBinding
import net.daum.mf.map.api.MapView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = binding.mainNavContainer.getFragment<Fragment>()
        val navController = navHostFragment.findNavController()
        val bottomNavBar = binding.bottomNavBar
        bottomNavBar.setupWithNavController(navController)

        /*val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.fragment_feed, R.id.fragment_recommend,
                R.id.fragment_map, R.id.fragment_my_page),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )*/

        fun onOptionsItemSelected(item: MenuItem): Boolean {
            return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }

        //val hashKey = getKeyHash(this)
        //println("해시키: $hashKey")

        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.configure(applicationContext)

            println("앰플리파이를 기동했습니다.")
        } catch (error: AmplifyException) {
            println("앰플리파이를 기동하지 못했습니다: $error")
        }
    }
}