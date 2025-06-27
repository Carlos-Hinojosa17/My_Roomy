package com.example.myroomy.admin.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myroomy.R
import com.example.myroomy.admin.adapters.UsuarioAdapter
import com.example.myroomy.admin.database.UsuarioDAO
import com.example.myroomy.admin.models.Usuario
import com.example.myroomy.databinding.FragmentAdminUsuarioListBinding

class UsuarioListFragment : Fragment() {

    private var _binding: FragmentAdminUsuarioListBinding? = null
    private val binding get() = _binding!!

    private lateinit var usuarioDAO: UsuarioDAO
    private lateinit var adapter: UsuarioAdapter
    private val listaUsuarios = mutableListOf<Usuario>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminUsuarioListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usuarioDAO = UsuarioDAO(requireContext())

        adapter = UsuarioAdapter(
            listaUsuarios,
            onEditar = { usuarioSeleccionado ->
                val fragment = UsuarioFormFragment.nuevaInstancia(usuarioSeleccionado)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.contenedor_admin, fragment)
                    .addToBackStack(null)
                    .commit()
            },
            onEliminar = { usuario ->
                eliminarUsuario(usuario)
            }
        )

        binding.recyclerUsuarios.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerUsuarios.adapter = adapter

        binding.btnAgregarUsuario.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedor_admin, UsuarioFormFragment())
                .addToBackStack(null)
                .commit()
        }

        cargarUsuarios()
    }

    override fun onResume() {
        super.onResume()
        cargarUsuarios()
    }

    private fun cargarUsuarios() {
        listaUsuarios.clear()
        listaUsuarios.addAll(usuarioDAO.obtenerTodos())
        adapter.notifyDataSetChanged()
        verificarListaVacia()
    }

    private fun eliminarUsuario(usuario: Usuario) {
        val alert = AlertDialog.Builder(requireContext())
            .setTitle("¿Eliminar usuario?")
            .setMessage("¿Estás seguro de eliminar a '${usuario.nombre}'?")
            .setPositiveButton("Sí") { _, _ ->
                val filasEliminadas = usuarioDAO.eliminar(usuario.id)
                if (filasEliminadas > 0) {
                    val index = listaUsuarios.indexOf(usuario)
                    if (index != -1) {
                        listaUsuarios.removeAt(index)
                        adapter.notifyItemRemoved(index)
                        verificarListaVacia()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
        alert.show()
    }

    private fun verificarListaVacia() {
        if (listaUsuarios.isEmpty()) {
            binding.recyclerUsuarios.visibility = View.GONE
            binding.layoutVacio.visibility = View.VISIBLE
        } else {
            binding.recyclerUsuarios.visibility = View.VISIBLE
            binding.layoutVacio.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
