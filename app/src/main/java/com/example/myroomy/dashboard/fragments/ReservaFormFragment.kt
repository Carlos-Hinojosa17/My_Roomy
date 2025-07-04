package com.example.myroomy.dashboard.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myroomy.databinding.FragmentReservaFormBinding
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
import android.widget.ArrayAdapter
import com.example.myroomy.dashboard.database.ReservaDAO
import com.example.myroomy.dashboard.models.Reserva
import java.text.SimpleDateFormat
import java.util.Date

class ReservaFormFragment : Fragment() {

    private lateinit var binding: FragmentReservaFormBinding
    private lateinit var reservaDAO: ReservaDAO
    private var habitacionId: Int = -1
    private var precioPorNoche: Double = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentReservaFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reservaDAO = ReservaDAO(requireContext())

        // Recibir argumentos
        arguments?.let {
            habitacionId = it.getInt("habitacion_id")
            precioPorNoche = it.getDouble("precio")
        }

        // Cargar opciones spinner
        val cantidadAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, (1..6).toList())
        cantidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCantidadPersonas.adapter = cantidadAdapter

        val metodoAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf("Efectivo", "Tarjeta", "Yape/Plin"))
        metodoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMetodoPago.adapter = metodoAdapter

        val dateListener = { target: EditText ->
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, y, m, d ->
                target.setText(String.format("%04d-%02d-%02d", y, m + 1, d))
                calcularTotal()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.inputFechaInicio.setOnClickListener { dateListener(binding.inputFechaInicio) }
        binding.inputFechaFin.setOnClickListener { dateListener(binding.inputFechaFin) }

        binding.btnConfirmarReserva.setOnClickListener {
            val nombre = binding.inputNombreCliente.text.toString()
            val dni = binding.inputDNICliente.text.toString()
            val inicio = binding.inputFechaInicio.text.toString()
            val fin = binding.inputFechaFin.text.toString()
            val metodoPago = binding.spinnerMetodoPago.selectedItem.toString()
            val cantidadPersonas = binding.spinnerCantidadPersonas.selectedItem.toString().toInt()
            val total = calcularTotal()

            if (nombre.isBlank() || dni.isBlank() || inicio.isBlank() || fin.isBlank()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fechaSolicitud = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            val reserva = Reserva(
                idUsuario = 1,  // ⚠️ Aquí deberías traer el usuario real logueado
                idHabitacion = habitacionId,
                fechaSolicitud = fechaSolicitud,
                fechaIngreso = inicio,
                fechaSalida = fin,
                total = total,
                metodoPago = metodoPago,
                cantidadPersonas = cantidadPersonas
            )

            reservaDAO.insertar(reserva)
            Toast.makeText(requireContext(), "Reserva confirmada", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun calcularTotal(): Double {
        val inicio = binding.inputFechaInicio.text.toString()
        val fin = binding.inputFechaFin.text.toString()
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDate = sdf.parse(inicio)
            val endDate = sdf.parse(fin)
            val days = TimeUnit.DAYS.convert(endDate.time - startDate.time, TimeUnit.MILLISECONDS)
            val total = (if (days == 0L) 1 else days) * precioPorNoche
            binding.txtTotal.text = "Total: S/ %.2f".format(total)
            total
        } catch (e: Exception) {
            0.0
        }
    }
}
