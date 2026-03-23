package com.example.taller1.ui.inicio

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.taller1.R
import com.example.taller1.ui.auth.LoginActivity
import com.example.taller1.ui.auth.RegisterActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnComienza = findViewById<View>(R.id.btn_comienza)
        val btnRegistrarse = findViewById<View>(R.id.btn_registrarse)

        btnComienza.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}