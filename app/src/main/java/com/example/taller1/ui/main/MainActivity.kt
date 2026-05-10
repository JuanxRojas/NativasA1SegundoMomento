package com.example.taller1.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.taller1.R
import com.example.taller1.SupabaseClient
import com.example.taller1.data.UsuarioRepository
import com.example.taller1.ui.auth.LoginActivity
import com.example.taller1.ui.main.admin.AdminFragment
import com.example.taller1.ui.main.admin.UsuariosFragment
import com.example.taller1.ui.main.perfil.PerfilFragment
import com.example.taller1.ui.main.productos.CarritoFragment
import com.example.taller1.ui.main.productos.CatalogoFragment
import com.example.taller1.ui.main.productos.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        bottomNav = findViewById(R.id.bottom_nav)
        navView = findViewById(R.id.nav_view)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.nav_open, R.string.nav_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        // Color rosa al ícono del drawer (como la profe)
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.red)

        cargarFragment(HomeFragment())
        bottomNav.selectedItemId = R.id.nav_home

        // Configura visibilidad del menú lateral según el rol
        configurarMenuPorRol(navView.menu)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home     -> cargarFragment(HomeFragment())
                R.id.nav_catalogo -> cargarFragment(CatalogoFragment())
                R.id.nav_carrito  -> cargarFragment(CarritoFragment())
                R.id.nav_perfil   -> cargarFragment(PerfilFragment())
            }
            true
        }

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.drawer_home     -> cargarFragment(HomeFragment())
                R.id.drawer_catalogo -> cargarFragment(CatalogoFragment())
                R.id.drawer_carrito  -> cargarFragment(CarritoFragment())
                R.id.drawer_perfil   -> cargarFragment(PerfilFragment())
                R.id.nav_favoritos   -> cargarFragment(HomeFragment()) // reemplaza si tienes FavoritosFragment
                R.id.nav_admin       -> cargarFragment(AdminFragment())
                R.id.nav_usuarios    -> cargarFragment(UsuariosFragment())
                R.id.nav_logout      -> cerrarSesion()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun configurarMenuPorRol(menu: Menu) {
        lifecycleScope.launch {
            val rol = UsuarioRepository.obtenerRolActual()
            android.util.Log.d("DEBUG_ROL", "Rol obtenido: $rol")

            runOnUiThread {
                when (rol) {
                    "admin" -> {
                        menu.findItem(R.id.nav_admin)?.isVisible = true
                        menu.findItem(R.id.nav_usuarios)?.isVisible = true
                    }
                    "vendedor" -> {
                        menu.findItem(R.id.nav_admin)?.isVisible = true
                        menu.findItem(R.id.nav_usuarios)?.isVisible = false
                    }
                    else -> {
                        menu.findItem(R.id.nav_admin)?.isVisible = false
                        menu.findItem(R.id.nav_usuarios)?.isVisible = false
                    }
                }
            }
        }
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun cerrarSesion() {
        lifecycleScope.launch {
            try {
                SupabaseClient.client.auth.signOut()
                runOnUiThread {
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finishAffinity()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}