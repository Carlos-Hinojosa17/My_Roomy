package com.example.myroomy.admin.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myroomy.R
import com.example.myroomy.admin.fragments.HabitacionListFragment
import com.example.myroomy.admin.fragments.UsuarioListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Cargar fragmento por defecto
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedor_admin, HabitacionListFragment())
            .commit()

        val nav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        nav.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_habitaciones -> HabitacionListFragment()
                R.id.nav_usuarios -> UsuarioListFragment()
                else -> null
            }

            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.contenedor_admin, fragment as Fragment)
                    .commit()
                true
            } else {
                false
            }
        }
    }
}
