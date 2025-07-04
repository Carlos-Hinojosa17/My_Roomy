package com.example.myroomy.dashboard.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myroomy.MainActivity
import com.example.myroomy.R
import com.example.myroomy.dashboard.adapters.HabitacionCatalogoAdapter
import com.example.myroomy.dashboard.fragments.CatalogoFragment
import com.example.myroomy.dashboard.fragments.HabitacionListFragment
import com.example.myroomy.dashboard.fragments.UsuarioListFragment

import com.example.myroomy.dashboard.fragments.ClienteHomeFragment
import com.example.myroomy.dashboard.fragments.PerfilFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.example.myroomy.dashboard.models.TipoUsuario

class DashboardActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: MaterialToolbar

    private var tipoUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_activity)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val prefs = getSharedPreferences("MyRoomyPrefs", MODE_PRIVATE)
        tipoUsuario = prefs.getString("usuario_tipo", TipoUsuario.CLIENTE.name)

        // Cargar menú dinámico
        navigationView.menu.clear()
        if (tipoUsuario == TipoUsuario.ADMIN.name) {
            navigationView.inflateMenu(R.menu.menu_drawer_admin)
        } else {
            navigationView.inflateMenu(R.menu.menu_drawer_cliente)
        }

        // Fragment inicial
        if (savedInstanceState == null) {
            if (tipoUsuario == TipoUsuario.ADMIN.name) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.contenedor_main, HabitacionListFragment())
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.contenedor_main, CatalogoFragment())
                    .commit()
            }
        }

        // Handle menú
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_gestion_reservas -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contenedor_main, HabitacionListFragment())
                        .commit()
                }
                R.id.nav_gestion_usuarios -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contenedor_main, UsuarioListFragment())
                        .commit()
                }
                R.id.nav_reservas -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contenedor_main, ClienteHomeFragment())
                        .commit()
                }
                R.id.nav_perfil -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contenedor_main, PerfilFragment())
                        .commit()
                }
                R.id.nav_cerrar_sesion -> {
                    cerrarSesion()
                }
                else -> {
                    Toast.makeText(this, "Opción no manejada", Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Mostrar datos en el header
        val headerView = navigationView.getHeaderView(0)
        val txtNombre = headerView.findViewById<TextView>(R.id.txtNombreUsuario)
        val txtCorreo = headerView.findViewById<TextView>(R.id.txtCorreoUsuario)

        val nombre = prefs.getString("usuario_nombre", "Nombre")
        val correo = prefs.getString("usuario_correo", "Correo")

        txtNombre.text = nombre
        txtCorreo.text = correo

        ViewCompat.setOnApplyWindowInsetsListener(headerView) { view, insets ->
            val topInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(
                view.paddingLeft,
                topInset + 16,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }
    }

    private fun cerrarSesion() {
        val prefs = getSharedPreferences("MyRoomyPrefs", MODE_PRIVATE)
        prefs.edit().clear().apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
