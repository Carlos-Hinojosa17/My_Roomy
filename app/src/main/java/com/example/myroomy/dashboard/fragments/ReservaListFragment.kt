package com.example.myroomy.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myroomy.R
import com.example.myroomy.dashboard.adapters.ReservaAdapter
import com.example.myroomy.dashboard.database.ReservaDAO
import com.example.myroomy.dashboard.models.Reserva

class ReservaListFragment : Fragment() {

    private lateinit var dao: ReservaDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dao = ReservaDAO(requireContext())
        val view = inflater.inflate(R.layout.fragment_reserva_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerReservas)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val lista = dao.obtenerPendientes()
        val adapter = ReservaAdapter(lista) { reserva, accion ->
            if (accion == "aceptar") {
                dao.actualizarEstado(reserva.id, "Aceptada", "Aprobado por el admin")
            } else if (accion == "rechazar") {
                dao.actualizarEstado(reserva.id, "Rechazada", "Rechazado por el admin")
            }
            // Refresh
            recyclerView.adapter = ReservaAdapter(dao.obtenerPendientes(), this::onAccion)
        }

        recyclerView.adapter = adapter

        return view
    }

    private fun onAccion(reserva: Reserva, accion: String) {
        if (accion == "aceptar") {
            dao.actualizarEstado(reserva.id, "Aceptada", "Aprobado por el admin")
        } else if (accion == "rechazar") {
            dao.actualizarEstado(reserva.id, "Rechazada", "Rechazado por el admin")
        }
        view?.findViewById<RecyclerView>(R.id.recyclerReservas)?.adapter =
            ReservaAdapter(dao.obtenerPendientes(), this::onAccion)
    }
}
