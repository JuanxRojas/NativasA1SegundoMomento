package com.example.taller1.ui.auth

import android.content.Intent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taller1.R
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.taller1.ui.main.MainActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.example.taller1.SupabaseClient
import com.example.taller1.data.CredencialesManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var tvIngresarConHuella: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_login)

        val iconBack = findViewById<View>(R.id.logo_login)
        iconBack.setOnClickListener {
            finish()
        }

        tvIngresarConHuella = findViewById(R.id.ingresar_huella)

        //inicio de sesion con huella
        tvIngresarConHuella.setOnClickListener {
            mostrarDialogoHuella()
        }


        // Listeners de los botones
        findViewById<android.widget.Button>(R.id.btn_ingresar)
            .setOnClickListener { iniciarSesion() }

        findViewById<android.widget.Button>(R.id.text_noCuenta)
            .setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))
            }

        findViewById<android.widget.Button>(R.id.btn_recuperarContraseña)
            .setOnClickListener {
                Toast.makeText(this, "Proximamente", Toast.LENGTH_SHORT).show()
            }

        findViewById<android.widget.LinearLayout>(R.id.btn_google)
            .setOnClickListener { iniciarSesionConGoogle() }
    }

    override fun onResume() {
        super.onResume()
        configurarVisibilidadHuella()
    }
    private fun configurarVisibilidadHuella() {
        //verificar si hay credenciales guardadas localmente
        val huellaActiva = CredencialesManager.huellaActiva(this)

        //Verificar si el dispositivo tiene sensor de huella
        val biometricManager = BiometricManager.from(this)
        val huellaDisponible = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.BIOMETRIC_WEAK
        ) == BiometricManager.BIOMETRIC_SUCCESS

        tvIngresarConHuella.visibility = if (huellaDisponible && huellaActiva) View.VISIBLE else View.GONE
//        val huellaDisponible = biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
//        tvIngresarConHuella.visibility = if (huellaDisponible && huellaActiva) View.VISIBLE else View.GONE
    }

    // 3.3 Función iniciarSesion()
    private fun iniciarSesion() {

        val correo = findViewById<EditText>(R.id.text_usuario)
            .text.toString().trim()

        val contrasena = findViewById<EditText>(R.id.Password_login)
            .text.toString()

        // Validaciones locales


        if (correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (contrasena.length < 6) {
            Toast.makeText(this, "La contraseña debe tener minimo 6 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        // Llamada a Supabase Auth

        lifecycleScope.launch {
            try {

                SupabaseClient.client.auth.signInWith(Email) {
                    email = correo
                    password = contrasena
                }

                CredencialesManager.guardarCredenciales(
                    context = this@LoginActivity,
                    correo = correo,
                    contrasena = contrasena,
                    huellaActiva = true  // ya inició sesión, habilita huella
                )

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finishAffinity()

            } catch (e: Exception) {

                val mensaje = when {
                    e.message?.contains("Invalid login credentials") == true ->
                        "Correo o contrasena incorrectos"
                    else -> "Error al iniciar sesion: ${e.message}"
                }

                Toast.makeText(this@LoginActivity, mensaje, Toast.LENGTH_LONG).show()
            }
        }
    }

    // 3.7 Función iniciarSesionConGoogle()
    private fun iniciarSesionConGoogle() {

        lifecycleScope.launch {
            try {

                // 1. Configurar la solicitud de Google

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("294050176929-pcheli8959hc748ukbqjg7asfulne6oo.apps.googleusercontent.com")
                    .setAutoSelectEnabled(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                // 2. Mostrar el selector de cuentas

                val credentialManager = CredentialManager.create(this@LoginActivity)

                val result = credentialManager.getCredential(
                    this@LoginActivity,
                    request
                )

                // 3. Obtener el token de Google

                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(result.credential.data)

                // 4. Enviar el token a Supabase

                SupabaseClient.client.auth.signInWith(IDToken) {
                    idToken = googleIdTokenCredential.idToken
                    provider = Google
                }

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))

            } catch (e: Exception) {

                Toast.makeText(
                    this@LoginActivity,
                    "Error al iniciar con Google: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
        private fun mostrarDialogoHuella () {
            val executor = ContextCompat.getMainExecutor(this)
            val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        val correo = CredencialesManager.obtenerCorreo(this@LoginActivity)
                        val contrasena = CredencialesManager.obtenerContrasena(this@LoginActivity)
                        android.util.Log.d("HUELLA", "correo: $correo, contrasena: $contrasena")
                        if (correo != null && contrasena != null) {
                            //singin credenciales normales
                            lifecycleScope.launch {
                                try {
                                    SupabaseClient.client.auth.signInWith(Email) {
                                        email = correo
                                        password = contrasena
                                    }
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            MainActivity::class.java
                                        )
                                    )
                                    finishAffinity()
                                } catch (e: Exception) {
                                    runOnUiThread {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Error al iniciar sesion: ${e.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        } else {
                            //no hay credenciales- no logueado previamente - limpiar-ocultar
                            Toast.makeText(
                                this@LoginActivity,
                                "No hay credenciales",
                                Toast.LENGTH_LONG
                            ).show()
                            CredencialesManager.limpiarCredenciales(this@LoginActivity)
                        }
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        if (errorCode != BiometricPrompt.ERROR_USER_CANCELED &&
                            errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON
                        ) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Error biometrico: $errString",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onAuthenticationFailed() {
                        Toast.makeText(
                            this@LoginActivity,
                            "Autenticacion fallida",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                })
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Acceso con huella")
                .setSubtitle("Usa tu huella dactilar")
                .setNegativeButtonText("Cancelar")
                .build()

            biometricPrompt.authenticate(promptInfo)




    }
    }
