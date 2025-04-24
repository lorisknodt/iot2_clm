package com.example.clm_iot2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.clm_iot2.databinding.ActivityMainBinding
import com.google.android.libraries.places.api.Places

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        Places.initialize(applicationContext, "AIzaSyDjXhpvF9CbRi_tZFf3xgBQA5rGX5WbiyA")

        /* binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        } */
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_products, R.id.nav_cart, R.id.nav_orders, R.id.nav_profile, R.id.nav_tracking), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        handleDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleDeepLink(intent) // Appelle ici aussi, au cas où l'app est déjà ouverte
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun handleDeepLink(intent: Intent?) {
        intent?.data?.let { uri ->
            if (uri.host == "payment-result") {
                val status = uri.getQueryParameter("status") // "success" ou "failure"
                val orderId = uri.getQueryParameter("orderId")?.toLongOrNull()

                if (status != null && orderId != null) {
                    val navController = findNavController(R.id.paymentFragment)
                    val bundle = bundleOf("status" to status, "orderId" to orderId)

                    if (status == "success") {
                        //navController.navigate(R.id.action_global_paymentSuccessFragment, bundle)

                        Log.d("MainActivity", "Succes du payement")
                    } else {
                        //navController.navigate(R.id.action_global_paymentFailureFragment, bundle)

                        Log.d("MainActivity", "Failure du payement")
                    }
                }
            }
        }
    }
}