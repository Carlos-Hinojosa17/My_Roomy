package com.example.myroomy.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.myroomy.R
import com.example.myroomy.dashboard.models.Reserva
import com.example.myroomy.databinding.FragmentReservaDetalleBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReservaDetalleBottomSheet(
    private val reserva: Reserva,
    private val onAccion: (Reserva, String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentReservaDetalleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReservaDetalleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtNombreUsuarioDetalle.text = reserva.nombreUsuario
        binding.txtHabitacionDetalle.text = "${reserva.nombreHabitacion} - S/ %.2f".format(reserva.precio)
        binding.txtFechasDetalle.text = "${reserva.fechaIngreso} al ${reserva.fechaSalida}"
        binding.txtPersonasDetalle.text = "${reserva.cantidadPersonas} persona(s)"
        binding.txtMetodoPagoDetalle.text = reserva.metodoPago
        binding.txtEstadoDetalle.text = reserva.estado

        val bg = when (reserva.estado.lowercase()) {
            "aceptada" -> requireContext().getDrawable(R.drawable.bg_chip_aceptada)
            "rechazada" -> requireContext().getDrawable(R.drawable.bg_chip_rechazada)
            "pendiente" -> requireContext().getDrawable(R.drawable.bg_chip_pendiente)
            else -> requireContext().getDrawable(R.drawable.bg_chip_estado)
        }

        binding.txtEstadoDetalle.background = bg
        binding.txtComentarioDetalle.text = reserva.comentario.ifBlank { "Sin comentarios" }

        // Mostrar botones solo si es Pendiente
        if (reserva.estado.equals("Pendiente", ignoreCase = true)) {
            binding.btnAceptarDetalle.visibility = View.VISIBLE
            binding.btnRechazarDetalle.visibility = View.VISIBLE
        } else {
            binding.btnAceptarDetalle.visibility = View.GONE
            binding.btnRechazarDetalle.visibility = View.GONE
        }

        binding.btnAceptarDetalle.setOnClickListener {
            onAccion(reserva, "aceptar")
            dismiss()
        }

        binding.btnRechazarDetalle.setOnClickListener {
            onAccion(reserva, "rechazar")
            dismiss()
        }

        binding.btnCerrarDetalle.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
