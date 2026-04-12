package com.example.taller1.ui.auth

import android.R.attr.password
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.taller1.R
import com.example.taller1.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlin.jvm.java

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNombres: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText
    private lateinit var etReContrasena: EditText
    private lateinit var checkTerminos: CheckBox
    private lateinit var btnRegistro: Button
    private lateinit var tvCuenta: Button


    @Serializable
    data class UsuarioData(
        val id: String,
        val nombres: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val btnLogin = findViewById<View>(R.id.btn_ir_login)

        btnLogin.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            finish()
        }
        val rootView = findViewById<ViewGroup>(R.id.main_register)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomPadding = maxOf(systemBars.bottom, imeInsets.bottom)
            val dp30 = (30 * resources.displayMetrics.density).toInt()
            v.setPadding(
                systemBars.left + dp30,
                systemBars.top + dp30,
                systemBars.right + dp30,
                bottomPadding + dp30
            )
            insets
        }

        etNombres = findViewById(R.id.input_nombre)
        etCorreo = findViewById(R.id.input_correo)
        etContrasena = findViewById(R.id.input_password)
        etReContrasena = findViewById(R.id.input_verificar_password)
        checkTerminos = findViewById(R.id.check_terminos)
        btnRegistro = findViewById(R.id.btn_registrarse)
        tvCuenta = findViewById(R.id.btn_ir_login)


        btnRegistro.setOnClickListener {
            val nombres = etNombres.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()
            val reContrasena = etReContrasena.text.toString().trim()

            //validaciones


            if (nombres.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || reContrasena.isEmpty()) {
                makeText(this, "Por favor rellena todos los campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (contrasena.length < 8) {
                makeText(
                    this,
                    "La contraseña debe tener al menos 8 caracteres",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (contrasena != reContrasena) {
                makeText(this, "Las contraseñas con coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!checkTerminos.isChecked) {
                makeText(this, "Debe aceptar los terminos y condiciones", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            //Registro supabase

            lifecycleScope.launch{
                try{
                    //registrar el user
                        val resultado = SupabaseClient.client.auth.signUpWith(Email){
                            email = correo
                            password = contrasena
                        }
                    // guardar los datos adicionales
                        val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: ""
                        SupabaseClient.client.postgrest["Usuarios"].insert(
                            UsuarioData(
                            id = userId,
                            nombres = nombres
                            )
                    )
                    //Redirigir al usuario al Login
                    runOnUiThread {
                        makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }


                }catch (e: Exception){
                    runOnUiThread {
                        makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

            }

            tvCuenta.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }





        }
    }
}