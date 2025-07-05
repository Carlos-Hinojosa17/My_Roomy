package com.example.myroomy.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myroomy.R
import com.example.myroomy.dashboard.adapters.ReservaAdapter
import com.example.myroomy.dashboard.database.ReservaDAO


class ReservaListFragment : Fragment() {

    private lateinit var dao: ReservaDAO
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dao = ReservaDAO(requireContext())
        val view = inflater.inflate(R.layout.fragment_reserva_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerReservas)
        recyclerView.layoutManager = LinearLayoutManager(context)
        spinner = view.findViewById(R.id.spinnerFiltro)

        val filtros = listOf("Pendiente", "Aceptada", "Rechazada", "Todas")
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, filtros)

        spinner.setSelection(0) // Por defecto "Pendiente"
        spinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long) {
                cargarReservas(filtros[position])
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        })

        return view
    }

    private fun cargarReservas(estado: String) {
        val lista = when (estado) {
            "Todas" -> dao.listarTodas()
            else -> dao.listarPorEstado(estado)
        }

        recyclerView.adapter = ReservaAdapter(lista) { reserva, accion ->
            if (accion == "aceptar") {
                dao.actualizarEstado(reserva.id, "Aceptada")
            } else if (accion == "rechazar") {
                dao.actualizarEstado(reserva.id, "Rechazada")
            }

            cargarReservas(spinner.selectedItem.toString())
        }
    }
}
