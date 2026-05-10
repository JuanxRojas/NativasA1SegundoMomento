package com.example.taller1.data

import android.content.Context

object CredencialesManager {
    private const val PREFS_NAME = "auth"
    private const val KEY_CORREO = "correo"
    private const val KEY_CONTRASENA = "contrasena"
    private const val KEY_HUELLA = "huella_activa"

    fun guardarCredenciales(context: Context, correo: String, contrasena: String, huellaActiva: Boolean) {
        android.util.Log.d("CREDS", "Guardando - correo: $correo, huella: $huellaActiva")
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_CORREO, correo)
            .putString(KEY_CONTRASENA, contrasena)
            .putBoolean(KEY_HUELLA, huellaActiva)
            .apply()
        android.util.Log.d("CREDS", "Guardado OK")
    }
    fun cerrarSesion(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val huella = prefs.getBoolean(KEY_HUELLA, false)
        prefs.edit()
            .remove(KEY_CORREO)      // ← borrá solo correo
            .remove(KEY_CONTRASENA)  // ← y contraseña
            .apply()                 // ← sin tocar KEY_HUELLA
    }
    fun limpiarCredenciales(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()

    fun huellaActiva(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_HUELLA, false)

    fun obtenerCorreo(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_CORREO, null)

    fun obtenerContrasena(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_CONTRASENA, null)
}