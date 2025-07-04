package com.example.myroomy.dashboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myroomy.R
import com.example.myroomy.dashboard.models.Reserva

class ReservaAdapter(
    private val reservas: List<Reserva>,
    private val onAccion: (Reserva, String) -> Unit
) : RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>() {

    inner class ReservaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtInfo: TextView = view.findViewById(R.id.txtInfoReserva)
        val txtEstado: TextView = view.findViewById(R.id.txtEstadoReserva)
        val btnAceptar: Button = view.findViewById(R.id.btnAceptar)
        val btnRechazar: Button = view.findViewById(R.id.btnRechazar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reserva, parent, false)
        return ReservaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservas[position]

        holder.txtInfo.text = "Reserva ID: ${reserva.id} | Usuario: ${reserva.idUsuario} | Hab: ${reserva.idHabitacion}"
        holder.txtEstado.text = "Estado: ${reserva.estado}"

        holder.btnAceptar.setOnClickListener {
            onAccion(reserva, "aceptar")
        }

        holder.btnRechazar.setOnClickListener {
            onAccion(reserva, "rechazar")
        }
    }

    override fun getItemCount(): Int = reservas.size
}
