package com.example.myroomy

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class RegistroActivity : AppCompatActivity() {

    private lateinit var etFecha: EditText
    private lateinit var etHora: EditText
    private lateinit var cbHora: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)  // <- CORREGIDO
        setContentView(R.layout.activity_registro)

        etFecha = findViewById(R.id.etFecha)
        etHora = findViewById(R.id.etHora)
        cbHora = findViewById(R.id.cbHora)

        // Spinner
        val spinner: Spinner = findViewById(R.id.spinnerOpciones)
        val opciones = arrayOf("Opción 1", "Opción 2", "Opción 3")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)

        // Calendario
        val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH)
        val day = calendario.get(Calendar.DAY_OF_MONTH)

        etFecha.setOnClickListener {
            DatePickerDialog(this, { _, y, m, d ->
                etFecha.setText(String.format("%02d/%02d/%04d", d, m + 1, y))
            }, year, month, day).show()
        }

        // Hora
        cbHora.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etHora.visibility = View.VISIBLE

                val hora = calendario.get(Calendar.HOUR_OF_DAY)
                val minuto = calendario.get(Calendar.MINUTE)

                TimePickerDialog(this, { _, h, m ->
                    etHora.setText(String.format("%02d:%02d", h, m))
                }, hora, minuto, true).show()
            } else {
                etHora.visibility = View.GONE
            }
        }

        // Botones
        findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnGuardar).setOnClickListener {
            Toast.makeText(this, "Reserva guardada (funcionalidad pendiente)", Toast.LENGTH_SHORT).show()
        }
    }
}
