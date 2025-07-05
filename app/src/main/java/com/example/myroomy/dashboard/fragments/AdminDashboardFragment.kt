package com.example.myroomy.dashboard.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myroomy.dashboard.database.HabitacionDAO
import com.example.myroomy.dashboard.database.ReservaDAO
import com.example.myroomy.dashboard.database.UsuarioDAO
import com.example.myroomy.databinding.FragmentAdminDashboardBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminDashboardFragment : Fragment() {

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val daoHabitacion = HabitacionDAO(requireContext())
            val daoReserva = ReservaDAO(requireContext())
            val daoUsuario = UsuarioDAO(requireContext())

            val habitaciones = withContext(Dispatchers.IO) { daoHabitacion.obtenerTodos() }
            val reservas = withContext(Dispatchers.IO) { daoReserva.listarTodas() }
            val usuarios = withContext(Dispatchers.IO) { daoUsuario.obtenerTodos() }

            // Actualizar textos
            binding.txtHabitacionesCount.text = "${habitaciones.size}"
            binding.txtReservasCount.text = "${reservas.size}"
            binding.txtUsuariosCount.text = "${usuarios.size}"

            // Calcular ocupación (porcentaje simple)
            val ocupadas = reservas.size
            val total = if (habitaciones.isNotEmpty()) habitaciones.size else 1
            val porcentajeOcupacion = (ocupadas * 100f) / total

            binding.txtOcupacionLabel.text = "Ocupación: ${porcentajeOcupacion.toInt()}%"

            // Configurar el pie chart
            setupPieChart(porcentajeOcupacion)
        }
    }

    private fun setupPieChart(porcentaje: Float) {
        val entries = listOf(
            PieEntry(porcentaje, "Ocupadas"),
            PieEntry(100f - porcentaje, "Libres")
        )

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(Color.parseColor("#3F51B5"), Color.LTGRAY)
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 14f

        val data = PieData(dataSet)

        binding.pieChartOcupacion.apply {
            this.data = data
            description.isEnabled = false
            legend.isEnabled = false
            setUsePercentValues(true)
            setHoleColor(Color.TRANSPARENT)
            setCenterText("${porcentaje.toInt()}%")
            setCenterTextSize(18f)
            animateY(1000)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
