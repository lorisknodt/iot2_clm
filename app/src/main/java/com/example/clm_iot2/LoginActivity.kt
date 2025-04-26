package com.example.clm_iot2

import RetrofitInstance
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.example.clm_iot2.data.network.RetrofitInstanceAuth
import com.example.clm_iot2.model.LoginRequest
import com.example.clm_iot2.security.TokenManager
import com.example.clm_iot2.singleton.CurrentUser
import com.example.clm_iot2.ui.register.RegisterFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var loadingCard : CardView
    private lateinit var mainContent : ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loadingCard = findViewById(R.id.loginLoadingCard)
        mainContent = findViewById(R.id.layout_login)

        // Vérifier si un token valide existe
        val token = TokenManager.getToken()
        Log.d("LoginActivity", "Voici el token; $token")
        // Si le token existe, rediriger directement vers la page principale
        if (!token.isNullOrEmpty()) {
            loadingCard.visibility = View.VISIBLE
            mainContent.visibility = View.GONE
            loadCurrentUser()
        }




        val emailEditText = findViewById<TextInputEditText>(R.id.et_username)
        val passwordEditText = findViewById<TextInputEditText>(R.id.et_password)

        val btnLogin = findViewById<MaterialButton>(R.id.btn_login)
        val btnMasi = findViewById<MaterialButton>(R.id.btn_login_masi)
        val btnRegister = findViewById<Button>(R.id.btn_register)



        btnLogin.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            } else {
                val loginRequest = LoginRequest(email, password)


                lifecycleScope.launch {
                    try {
                        val response = RetrofitInstanceAuth.api.login(loginRequest)

                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            if (loginResponse != null) {
                                CurrentUser.login(loginResponse.clientId)

                                val secureStorage = TokenManager
                                secureStorage.saveToken(loginResponse.token)

                                loadCurrentUser()
                            }
                            Toast.makeText(this@LoginActivity, "Connexion réussie", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LoginActivity, "Échec de la connexion : ${response.code()}", Toast.LENGTH_SHORT).show()
                            Log.d("LoginActivity", "voici l\'erreur: ${response}")
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@LoginActivity, "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.d("LoginActivity", "voici l\'erreur: ${e.message}")

                    }
                }
            }
        }


        btnMasi.setOnClickListener {
            // Rediriger vers MainActivity (comme avant)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnRegister.setOnClickListener {
            // Afficher le fragment d'inscription
            showRegisterFragment()
        }
    }

    private fun loadCurrentUser() {
        lifecycleScope.launch {
            try {
                // Récupérer les informations de l'utilisateur avec le token
                val user = RetrofitInstance.getApi().getCurrentUserDetails()
                Log.d("LoginActivity", "Voici le user: $user")
                CurrentUser.setUser(user)

                // Après avoir mis à jour CurrentUser, on lance MainActivity
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                loadingCard.visibility = View.GONE

                finish()
            } catch (e: Exception) {
                Log.d("LoginActivity", "Erreur lors de la récupération de l'utilisateur: ${e.message}")
                // Toast.makeText(this@LoginActivity, "Erreur lors de l'initialisation", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showRegisterFragment() {
        // Masquer les éléments de l'écran de connexion
        findViewById<CardView>(R.id.card_login).visibility = View.GONE
        findViewById<ImageView>(R.id.iv_app_logo).visibility = View.GONE
        findViewById<TextView>(R.id.tv_app_name).visibility = View.GONE
        findViewById<TextView>(R.id.tv_subtitle).visibility = View.GONE
        findViewById<TextView>(R.id.tv_help).visibility = View.GONE

        // Rendre le conteneur de fragment visible
        findViewById<FrameLayout>(R.id.register_fragment_container).visibility = View.VISIBLE

        val registerFragment = RegisterFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.register_fragment_container, registerFragment)
            .addToBackStack("register_fragment")
            .commit()
    }
    // Ajoutez cette méthode à votre LoginActivity
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            // Rétablir la visibilité des éléments de l'écran de connexion
            findViewById<CardView>(R.id.card_login).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.iv_app_logo).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_app_name).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_subtitle).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_help).visibility = View.VISIBLE

            // Masquer le conteneur de fragment
            findViewById<FrameLayout>(R.id.register_fragment_container).visibility = View.GONE

            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}