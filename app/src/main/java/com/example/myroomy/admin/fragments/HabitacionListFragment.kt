package com.example.myroomy.admin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myroomy.R
import com.example.myroomy.admin.adapters.HabitacionAdapter
import com.example.myroomy.admin.database.HabitacionDAO
import com.example.myroomy.admin.models.Habitacion
import com.example.myroomy.databinding.FragmentAdminHabitacionListBinding


class HabitacionListFragment : Fragment() {

    private var _binding: FragmentAdminHabitacionListBinding? = null
    private val binding get() = _binding!!

    private lateinit var habitacionDAO: HabitacionDAO
    private lateinit var adapter: HabitacionAdapter
    private val listaHabitaciones = mutableListOf<Habitacion>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminHabitacionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habitacionDAO = HabitacionDAO(requireContext())

        adapter = HabitacionAdapter(
            listaHabitaciones,
            onItemClick = { habitacionSeleccionada ->
                val fragment = HabitacionFormFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable("habitacion", habitacionSeleccionada)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.contenedor_admin, fragment)
                    .addToBackStack(null)
                    .commit()
            },
            onEliminarClick = { habitacion ->
                eliminarHabitacion(habitacion)
            }
        )

        binding.recyclerHabitaciones.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHabitaciones.adapter = adapter

        binding.btnAgregarHabitacion.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedor_admin, HabitacionFormFragment())
                .addToBackStack(null)
                .commit()
        }

        cargarHabitaciones()
    }

    override fun onResume() {
        super.onResume()
        cargarHabitaciones()
    }

    private fun cargarHabitaciones() {
        listaHabitaciones.clear()
        listaHabitaciones.addAll(habitacionDAO.obtenerTodos())
        adapter.notifyDataSetChanged()
        verificarListaVacia()
    }

    private fun eliminarHabitacion(habitacion: Habitacion) {
        val alert = AlertDialog.Builder(requireContext())
            .setTitle("¿Eliminar habitación?")
            .setMessage("¿Estás seguro de eliminar '${habitacion.nombre}'?")
            .setPositiveButton("Sí") { _, _ ->
                val filasEliminadas = habitacionDAO.eliminar(habitacion.id)
                if (filasEliminadas > 0) {
                    val index = listaHabitaciones.indexOf(habitacion)
                    if (index != -1) {
                        listaHabitaciones.removeAt(index)
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
        if (listaHabitaciones.isEmpty()) {
            binding.recyclerHabitaciones.visibility = View.GONE
            binding.layoutVacio.visibility = View.VISIBLE
        } else {
            binding.recyclerHabitaciones.visibility = View.VISIBLE
            binding.layoutVacio.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
