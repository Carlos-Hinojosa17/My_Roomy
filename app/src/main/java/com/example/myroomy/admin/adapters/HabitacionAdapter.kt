package com.example.myroomy.admin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myroomy.R
import com.example.myroomy.admin.models.Habitacion
import com.bumptech.glide.Glide
import java.io.File

class HabitacionAdapter(
    private val lista: MutableList<Habitacion>,
    private val onItemClick: (Habitacion) -> Unit,
    private val onEliminarClick: (Habitacion) -> Unit
) : RecyclerView.Adapter<HabitacionAdapter.HabitacionViewHolder>() {

    class HabitacionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgHabitacion)
        val txtNombre: TextView = view.findViewById(R.id.txtNombreHabitacion)
        val txtCategoria: TextView = view.findViewById(R.id.txtCategoriaHabitacion)
        val txtEstado: TextView = view.findViewById(R.id.txtEstadoHabitacion)
        val txtPiso: TextView = view.findViewById(R.id.txtPisoHabitacion) // 🆕
        val txtPrecio: TextView = view.findViewById(R.id.txtPrecioHabitacion) // 🆕
        val btnEliminar: ImageButton = view.findViewById(R.id.btnEliminarHabitacion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitacionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habitacion, parent, false)
        return HabitacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitacionViewHolder, position: Int) {
        val h = lista[position]

        holder.txtNombre.text = h.nombre
        holder.txtCategoria.text = "Categoría: ${h.categoria}"
        holder.txtEstado.text = "Estado: ${h.estado}"
        holder.txtPiso.text = "Piso: ${h.piso}"
        holder.txtPrecio.text = "S/ ${"%.2f".format(h.precio)}"

        val context = holder.itemView.context
        if (h.imagen.startsWith("/")) {
            Glide.with(context).load(File(h.imagen)).into(holder.img)
        } else {
            val idDrawable = context.resources.getIdentifier(h.imagen, "drawable", context.packageName)
            holder.img.setImageResource(idDrawable.takeIf { it != 0 } ?: R.drawable.ic_image_not_found)
        }

        holder.itemView.setOnClickListener { onItemClick(h) }
        holder.btnEliminar.setOnClickListener { onEliminarClick(h) }
    }


    override fun getItemCount(): Int {
        return lista.size
    }

}


