package com.example.taller1.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.taller1.R
import com.example.taller1.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val iconBack = findViewById<View>(R.id.logo_login)
        val btnRegister = findViewById<View>(R.id.text_noCuenta)
        val btnIngresar = findViewById<View>(R.id.btn_ingresar)

        iconBack.setOnClickListener {
            finish()
        }
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

        }
        btnIngresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}