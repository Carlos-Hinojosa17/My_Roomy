package com.example.myroomy.admin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myroomy.R
import com.example.myroomy.admin.models.Usuario

class UsuarioAdapter(
    private val lista: MutableList<Usuario>,
    private val onEditar: (Usuario) -> Unit,
    private val onEliminar: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    inner class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombreUsuario)
        val txtCorreo: TextView = view.findViewById(R.id.txtCorreoUsuario)
        val txtTipo: TextView = view.findViewById(R.id.txtTipoUsuario)
        val btnEliminar: ImageButton = view.findViewById(R.id.btnEliminarUsuario)
        val cardView: View = view // Toda la vista del ítem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = lista[position]

        holder.txtNombre.text = usuario.nombre
        holder.txtCorreo.text = usuario.correo
        holder.txtTipo.text = usuario.tipo.toString() // si es string, va directo

        // Clic en toda la tarjeta
        holder.cardView.setOnClickListener {
            onEditar(usuario)
        }

        // Botón eliminar
        holder.btnEliminar.setOnClickListener {
            onEliminar(usuario)
        }
    }

    override fun getItemCount(): Int = lista.size
}
