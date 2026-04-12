package com.example.taller1.ui.main.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.taller1.R
import com.example.taller1.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class PerfilFragment : Fragment() {

    private lateinit var etNombres: EditText
    private lateinit var etApellidos: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRePassword: EditText
    private lateinit var btnGuardar: Button

    @Serializable
    data class UsuarioUpdate(
        val nombres: String,
        val apellidos: String
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etNombres = view.findViewById(R.id.input_editar_nombres)
        etApellidos = view.findViewById(R.id.input_editar_apellidos)
        etCorreo = view.findViewById(R.id.input_editar_correo)
        etPassword = view.findViewById(R.id.input_editar_password)
        etRePassword = view.findViewById(R.id.input_editar_verificar_password)
        btnGuardar = view.findViewById(R.id.btn_guardar_perfil)

        cargarDatosUsuario()

        btnGuardar.setOnClickListener {
            guardarCambios()
        }
    }

    private fun cargarDatosUsuario() {
        lifecycleScope.launch {
            try {
                val user = SupabaseClient.client.auth.currentUserOrNull()
                if (user != null) {
                    etCorreo.setText(user.email)

                    val resultado = SupabaseClient.client.postgrest
                        .from("Usuarios")
                        .select(Columns.ALL) {
                            filter {
                                eq("id", user.id)
                            }
                        }
                        .decodeSingle<UsuarioUpdate>()

                    etNombres.setText(resultado.nombres)
                    etApellidos.setText(resultado.apellidos)
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al cargar datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardarCambios() {
        val nombres = etNombres.text.toString().trim()
        val apellidos = etApellidos.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val rePassword = etRePassword.text.toString().trim()

        if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor rellena todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isNotEmpty()) {
            if (password.length < 8) {
                Toast.makeText(requireContext(), "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                return
            }
            if (password != rePassword) {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return
            }
        }

        lifecycleScope.launch {
            try {
                val user = SupabaseClient.client.auth.currentUserOrNull()
                if (user != null) {

                    SupabaseClient.client.postgrest
                        .from("Usuarios")
                        .update(UsuarioUpdate(nombres = nombres, apellidos = apellidos)) {
                            filter {
                                eq("id", user.id)
                            }
                        }

                    if (correo != user.email) {
                        SupabaseClient.client.auth.updateUser {
                            email = correo
                        }
                    }

                    if (password.isNotEmpty()) {
                        SupabaseClient.client.auth.updateUser {
                            this.password = password
                        }
                    }

                    Toast.makeText(requireContext(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}