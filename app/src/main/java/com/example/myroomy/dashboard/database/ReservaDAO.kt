package com.example.myroomy.dashboard.database

import android.content.ContentValues
import android.content.Context
import com.example.myroomy.dashboard.models.Reserva

class ReservaDAO(context: Context) {
    private val dbHelper = AdminDatabaseHelper(context)

    fun insertar(reserva: Reserva): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id_usuario", reserva.idUsuario)
            put("id_habitacion", reserva.idHabitacion)
            put("fecha_solicitud", reserva.fechaSolicitud)
            put("estado", reserva.estado)
            put("comentario", reserva.comentario)
            put("fecha_ingreso", reserva.fechaIngreso)
            put("fecha_salida", reserva.fechaSalida)
            put("total", reserva.total)
            put("metodo_pago", reserva.metodoPago)
            put("cantidad_personas", reserva.cantidadPersonas)
        }
        return db.insert("reservas", null, values)
    }

    fun obtenerPendientes(): List<Reserva> {
        val db = dbHelper.readableDatabase
        val lista = mutableListOf<Reserva>()
        val cursor = db.rawQuery("SELECT * FROM reservas WHERE estado = 'Pendiente'", null)

        while (cursor.moveToNext()) {
            lista.add(
                Reserva(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario")),
                    idHabitacion = cursor.getInt(cursor.getColumnIndexOrThrow("id_habitacion")),
                    fechaSolicitud = cursor.getString(cursor.getColumnIndexOrThrow("fecha_solicitud")),
                    estado = cursor.getString(cursor.getColumnIndexOrThrow("estado")),
                    comentario = cursor.getString(cursor.getColumnIndexOrThrow("comentario")),
                    fechaIngreso = cursor.getString(cursor.getColumnIndexOrThrow("fecha_ingreso")),
                    fechaSalida = cursor.getString(cursor.getColumnIndexOrThrow("fecha_salida")),
                    total = cursor.getDouble(cursor.getColumnIndexOrThrow("total")),
                    metodoPago = cursor.getString(cursor.getColumnIndexOrThrow("metodo_pago")),
                    cantidadPersonas = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad_personas"))
                )
            )
        }
        cursor.close()
        return lista
    }

    fun actualizarEstado(idReserva: Int, nuevoEstado: String, comentario: String = ""): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("estado", nuevoEstado)
            put("comentario", comentario)
        }
        return db.update("reservas", values, "id = ?", arrayOf(idReserva.toString()))
    }
}
