package com.example.myroomy.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myroomy.dashboard.adapters.DestacadosAdapter
import com.example.myroomy.dashboard.adapters.HabitacionCatalogoAdapter
import com.example.myroomy.dashboard.database.HabitacionDAO
import com.example.myroomy.dashboard.models.Habitacion
import com.example.myroomy.databinding.FragmentCatalogoBinding
import com.google.android.material.chip.Chip

class CatalogoFragment : Fragment() {

    private var _binding: FragmentCatalogoBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterCatalogo: HabitacionCatalogoAdapter
    private lateinit var listaHabitaciones: List<Habitacion>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = HabitacionDAO(requireContext())
        listaHabitaciones = dao.obtenerTodos()

        // ViewPager2 destacados
        val destacados = listaHabitaciones.filter { it.categoria == "Suite" || it.precio > 200 }
        binding.viewPagerDestacados.adapter = DestacadosAdapter(destacados)

        // RecyclerView catálogo
        adapterCatalogo = HabitacionCatalogoAdapter(listaHabitaciones.toMutableList()) { habitacion ->
            // TODO: abrir detalle / dialog para reserva
        }
        binding.recyclerCatalogo.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerCatalogo.adapter = adapterCatalogo

        // Filtros dinámicos
        val categorias = listaHabitaciones.map { it.categoria }.distinct()
        categorias.forEach { cat ->
            val chip = Chip(requireContext()).apply {
                text = cat
                isCheckable = true
                setOnCheckedChangeListener { _, _ ->
                    aplicarFiltros()
                }
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

        adapterCatalogo.actualizarDatos(filtrado)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
