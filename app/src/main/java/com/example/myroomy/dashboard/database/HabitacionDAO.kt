package com.example.myroomy.dashboard.database

import android.content.ContentValues
import android.content.Context
import com.example.myroomy.dashboard.models.Habitacion

class HabitacionDAO(context: Context) {

    private val dbHelper = AdminDatabaseHelper(context)

    fun insertar(habitacion: Habitacion): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", habitacion.nombre)
            put("descripcion", habitacion.descripcion)
            put("categoria", habitacion.categoria)
            put("imagen", habitacion.imagen)
            put("estado", habitacion.estado)
            put("precio", habitacion.precio)
            put("servicios", habitacion.servicios.joinToString(","))
            put("numeroHabitacion", habitacion.numeroHabitacion)
            put("piso", habitacion.piso)
        }
        return db.insert("habitaciones", null, values)
    }

    fun actualizar(habitacion: Habitacion): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", habitacion.nombre)
            put("descripcion", habitacion.descripcion)
            put("categoria", habitacion.categoria)
            put("imagen", habitacion.imagen)
            put("estado", habitacion.estado)
            put("precio", habitacion.precio)
            put("servicios", habitacion.servicios.joinToString(","))
            put("numeroHabitacion", habitacion.numeroHabitacion)
            put("piso", habitacion.piso)
        }
        return db.update(
            "habitaciones",
            values,
            "id = ?",
            arrayOf(habitacion.id.toString())
        )
    }

    fun obtenerTodos(): List<Habitacion> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM habitaciones", null)

        val habitaciones = mutableListOf<Habitacion>()
        if (cursor.moveToFirst()) {
            do {
                val habitacion = Habitacion(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                    categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria")),
                    imagen = cursor.getString(cursor.getColumnIndexOrThrow("imagen")),
                    estado = cursor.getString(cursor.getColumnIndexOrThrow("estado")),
                    precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio")),
                    servicios = cursor.getString(cursor.getColumnIndexOrThrow("servicios")).split(","),
                    numeroHabitacion = cursor.getString(cursor.getColumnIndexOrThrow("numeroHabitacion")),
                    piso = cursor.getInt(cursor.getColumnIndexOrThrow("piso"))
                )
                habitaciones.add(habitacion)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return habitaciones
    }

    fun eliminar(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete("habitaciones", "id = ?", arrayOf(id.toString()))
    }
}
