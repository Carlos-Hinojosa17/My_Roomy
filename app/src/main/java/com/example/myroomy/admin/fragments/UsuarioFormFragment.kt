package com.example.myroomy.admin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myroomy.admin.database.UsuarioDAO
import com.example.myroomy.admin.models.TipoUsuario
import com.example.myroomy.admin.models.Usuario
import com.example.myroomy.databinding.FragmentAdminUsuarioFormBinding

class UsuarioFormFragment : Fragment() {

    private var _binding: FragmentAdminUsuarioFormBinding? = null
    private val binding get() = _binding!!

    private var usuario: Usuario? = null
    private lateinit var dao: UsuarioDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usuario = arguments?.getParcelable("usuario")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminUsuarioFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dao = UsuarioDAO(requireContext())

        val tipos = TipoUsuario.values().map {
            it.name.lowercase().replaceFirstChar(Char::titlecase)
        }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            tipos
        )
        binding.spinnerTipoUsuario.setAdapter(adapter)

        // Si viene para editar
        usuario?.let {
            binding.edtNombreUsuario.setText(it.nombre)
            binding.edtCorreoUsuario.setText(it.correo)
            binding.spinnerTipoUsuario.setText(
                it.tipo.name.lowercase().replaceFirstChar(Char::titlecase),
                false
            )
            binding.btnGuardarUsuario.text = "Actualizar usuario"
            binding.txtTituloFormularioUsuario.text = "Editar usuario"
        }

        binding.btnGuardarUsuario.setOnClickListener {
            val nombre = binding.edtNombreUsuario.text.toString().trim()
            val correo = binding.edtCorreoUsuario.text.toString().trim()
            val tipoTexto = binding.spinnerTipoUsuario.text.toString().trim()

            if (nombre.isEmpty() || correo.isEmpty() || tipoTexto.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tipo = TipoUsuario.values().firstOrNull {
                it.name.equals(tipoTexto, ignoreCase = true)
            }

            if (tipo == null) {
                Toast.makeText(requireContext(), "Tipo de usuario inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevo = Usuario(
                id = usuario?.id ?: 0,
                nombre = nombre,
                correo = correo,
                tipo = tipo,
                estado = 1
            )

            if (usuario == null) {
                dao.insertar(nuevo)
                Toast.makeText(requireContext(), "Usuario registrado", Toast.LENGTH_SHORT).show()
            } else {
                dao.actualizar(nuevo)
                Toast.makeText(requireContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show()
            }

            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun nuevaInstancia(usuario: Usuario? = null): UsuarioFormFragment {
            val fragment = UsuarioFormFragment()
            fragment.arguments = Bundle().apply {
                putParcelable("usuario", usuario)
            }
            return fragment
        }
    }
}
