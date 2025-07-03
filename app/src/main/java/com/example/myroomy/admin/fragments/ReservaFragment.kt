package com.example.myroomy.admin.fragments

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myroomy.admin.database.AdminDatabaseHelper
import com.example.myroomy.databinding.FragmentReservaFormBinding
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class ReservaFragment : Fragment() {

    private lateinit var binding: FragmentReservaFormBinding
    private lateinit var dbHelper: AdminDatabaseHelper
    private var habitacionId: Int = -1
    private var precioPorNoche: Double = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentReservaFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = AdminDatabaseHelper(requireContext())

        // Recibe argumentos de la habitación
        arguments?.let {
            habitacionId = it.getInt("habitacion_id")
            precioPorNoche = it.getDouble("precio")
        }

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

            val total = calcularTotal()
            if (nombre.isBlank() || dni.isBlank() || inicio.isBlank() || fin.isBlank()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = dbHelper.writableDatabase
            db.execSQL("""
                INSERT INTO reservas (habitacion_id, cliente_nombre, cliente_dni, fecha_inicio, fecha_fin, total)
                VALUES (?, ?, ?, ?, ?, ?)
            """, arrayOf(habitacionId, nombre, dni, inicio, fin, total))

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
            val total = days * precioPorNoche
            binding.txtTotal.text = "Total: S/ %.2f".format(total)
            total
        } catch (e: Exception) {
            0.0
        }
    }
}
