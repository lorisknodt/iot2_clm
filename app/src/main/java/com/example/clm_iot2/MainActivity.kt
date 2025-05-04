package com.example.clm_iot2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
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
import com.example.clm_iot2.security.TokenManager
import com.example.clm_iot2.singleton.CurrentUser
import com.google.android.libraries.places.api.Places

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1️⃣ INITIALISER LE BINDING EN PREMIER
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2️⃣ Ensuite, tu peux utiliser les vues
        setSupportActionBar(binding.appBarMain.toolbar)

        val navView = binding.navView
        val headerView = navView.getHeaderView(0)

        val userNameTextView = headerView.findViewById<TextView>(R.id.userNameTextView)
        val userEmailTextView = headerView.findViewById<TextView>(R.id.userEmailTextView)

        userNameTextView.text = CurrentUser.getUser()?.name ?: "Nom par défaut"
        userEmailTextView.text = CurrentUser.getUser()?.email ?: "Email par défaut"

        // 3️⃣ Initialisation Places
        Places.initialize(applicationContext, "AIzaSyDjXhpvF9CbRi_tZFf3xgBQA5rGX5WbiyA")

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_products, R.id.nav_cart, R.id.nav_orders, R.id.nav_profile),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // 4️⃣ Gestion du deep link
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