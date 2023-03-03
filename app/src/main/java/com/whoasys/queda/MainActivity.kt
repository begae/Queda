package com.whoasys.queda

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.whoasys.queda.databinding.ActivityMainBinding

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
                R.id.navigation_feed, R.id.navigation_recommend,
                R.id.navigation_map, R.id.navigation_my_page),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )*/

        fun onOptionsItemSelected(item: MenuItem): Boolean {
            return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }

        try {
            // Add these lines to add the AWSCognitoAuthPlugin and AWSS3StoragePlugin plugins
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.configure(applicationContext)

            println("Initialized Amplify")
        } catch (error: AmplifyException) {
            println("Could not initialize Amplify: $error")
        }
    }
}