package com.example.myroomy.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myroomy.R

class PerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        val prefs = requireContext().getSharedPreferences("MyRoomyPrefs", 0)
        val nombre = prefs.getString("usuario_nombre", "Nombre no disponible")
        val correo = prefs.getString("usuario_correo", "Correo no disponible")
        val fotoUrl = prefs.getString("usuario_foto", "")

        val txtNombre = view.findViewById<TextView>(R.id.txtNombrePerfil)
        val txtCorreo = view.findViewById<TextView>(R.id.txtCorreoPerfil)
        val imgFoto = view.findViewById<ImageView>(R.id.imgFotoPerfil)

        txtNombre.text = nombre
        txtCorreo.text = correo

        if (!fotoUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(fotoUrl)
                .placeholder(R.drawable.ic_person_placeholder)
                .into(imgFoto)
        } else {
            imgFoto.setImageResource(R.drawable.ic_person_placeholder)
        }

        return view
    }
}
