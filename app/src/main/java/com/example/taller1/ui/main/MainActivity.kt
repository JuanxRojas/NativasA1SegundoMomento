package com.example.taller1.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.taller1.R
import com.example.taller1.ui.main.productos.CatalogoFragment
import com.example.taller1.ui.main.productos.CarritoFragment
import com.example.taller1.ui.main.productos.HomeFragment
import com.example.taller1.ui.main.perfil.PerfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    // pos estas son las vistas jeje
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
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        cargarFragment(HomeFragment())


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
            }
            drawerLayout.closeDrawer(GravityCompat.START) // cierra el menú lateral
            true
        }
    }


    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


}