package com.example.myroomy.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myroomy.dashboard.adapters.DestacadosAdapter
import com.example.myroomy.dashboard.adapters.HabitacionCatalogoAdapter
import com.example.myroomy.dashboard.database.HabitacionDAO
import com.example.myroomy.dashboard.models.Habitacion
import com.example.myroomy.databinding.FragmentCatalogoBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatalogoFragment : Fragment() {

    private var _binding: FragmentCatalogoBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterCatalogo: HabitacionCatalogoAdapter
    private var listaHabitaciones: List<Habitacion> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterCatalogo = HabitacionCatalogoAdapter { habitacion ->
            mostrarDetalleReserva(habitacion)
        }

        binding.recyclerCatalogo.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerCatalogo.adapter = adapterCatalogo

        lifecycleScope.launch {
            val dao = HabitacionDAO(requireContext())
            val habitaciones = withContext(Dispatchers.IO) {
                dao.obtenerTodos()
            }
            listaHabitaciones = habitaciones
            configurarDestacados()
            configurarFiltros()
            adapterCatalogo.submitList(listaHabitaciones)
        }
    }

    private fun configurarDestacados() {
        val destacados = listaHabitaciones.filter { it.categoria == "Suite" || it.precio > 200 }
        binding.viewPagerDestacados.adapter = DestacadosAdapter(destacados) { habitacion ->
            mostrarDetalleReserva(habitacion)
        }
    }

    private fun mostrarDetalleReserva(habitacion: Habitacion) {
        val bottomSheet = DetalleReservaBottomSheet.nuevaInstancia(habitacion)
        bottomSheet.show(parentFragmentManager, "DetalleReserva")
    }

    private fun configurarFiltros() {
        val categorias = listaHabitaciones.map { it.categoria }.distinct()
        categorias.forEach { cat ->
            val chip = Chip(requireContext()).apply {
                text = cat
                isCheckable = true
                setOnCheckedChangeListener { _, _ -> aplicarFiltros() }
            }
            binding.chipGroupFiltros.addView(chip)
        }
    }

    private fun aplicarFiltros() {
        val seleccionados = binding.chipGroupFiltros.checkedChipIds.mapNotNull { id ->
            binding.chipGroupFiltros.findViewById<Chip>(id)?.text?.toString()
        }

        val filtrado = if (seleccionados.isEmpty()) {
            listaHabitaciones
        } else {
            listaHabitaciones.filter { seleccionados.contains(it.categoria) }
        }

        adapterCatalogo.submitList(filtrado)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
