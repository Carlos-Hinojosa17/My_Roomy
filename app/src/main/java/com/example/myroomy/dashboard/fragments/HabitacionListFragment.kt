package com.example.myroomy.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myroomy.R
import com.example.myroomy.dashboard.adapters.HabitacionAdapter
import com.example.myroomy.dashboard.database.HabitacionDAO
import com.example.myroomy.dashboard.models.Habitacion
import com.example.myroomy.dashboard.utils.FragmentHelper
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
                FragmentHelper.replaceWithSmoothSlide(
                    requireActivity() as AppCompatActivity,
                    HabitacionFormFragment.nuevaInstancia(habitacionSeleccionada)
                )
            },
            onEliminarClick = { habitacion ->
                confirmarEliminacion(habitacion)
            }
        )

        binding.recyclerHabitaciones.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHabitaciones.adapter = adapter

        binding.btnAgregarHabitacion.setOnClickListener {
            FragmentHelper.replaceWithSmoothSlide(
                requireActivity() as AppCompatActivity,
                HabitacionFormFragment.nuevaInstancia()
            )
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

    private fun confirmarEliminacion(habitacion: Habitacion) {
        AlertDialog.Builder(requireContext())
            .setTitle("¿Eliminar habitación?")
            .setMessage("¿Estás seguro de eliminar '${habitacion.nombre}'?")
            .setPositiveButton("Sí") { _, _ ->
                eliminarHabitacion(habitacion)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarHabitacion(habitacion: Habitacion) {
        val filasEliminadas = habitacionDAO.eliminar(habitacion.id)
        if (filasEliminadas > 0) {
            // Buscar por ID para mayor seguridad
            val index = listaHabitaciones.indexOfFirst { it.id == habitacion.id }
            if (index != -1) {
                listaHabitaciones.removeAt(index)
                adapter.notifyItemRemoved(index)
                verificarListaVacia()
                Toast.makeText(requireContext(), "Habitación eliminada", Toast.LENGTH_SHORT).show()
            } else {
                // Refrescar por si acaso no se encontró
                cargarHabitaciones()
            }
        } else {
            Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
        }
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
