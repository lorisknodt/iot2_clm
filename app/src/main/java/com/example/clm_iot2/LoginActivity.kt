package com.example.clm_iot2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btn_login_masi)
        btnLogin.setOnClickListener {
            // Rediriger vers la MainActivity pour l'instant
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Fermer l'activité de login
        }
    }
}