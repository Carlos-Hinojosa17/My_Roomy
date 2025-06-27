package com.example.myroomy.admin.database

import android.content.ContentValues
import android.content.Context
import com.example.myroomy.admin.models.Usuario
import com.example.myroomy.admin.models.TipoUsuario

class UsuarioDAO(context: Context) {
    private val dbHelper = AdminDatabaseHelper(context)

    fun insertar(usuario: Usuario): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", usuario.nombre)
            put("correo", usuario.correo)
            put("tipo", usuario.tipo.valor)
            put("estado", usuario.estado)
        }
        return db.insert("usuarios", null, values)
    }

    fun obtenerTodos(): List<Usuario> {
        val lista = mutableListOf<Usuario>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios", null)

        if (cursor.moveToFirst()) {
            do {
                val usuario = Usuario(
                    id = cursor.getInt(0),
                    nombre = cursor.getString(1),
                    correo = cursor.getString(2),
                    tipo = TipoUsuario.from(cursor.getString(3)),
                    estado = cursor.getInt(4)
                )
                lista.add(usuario)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return lista
    }

    fun actualizar(usuario: Usuario): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", usuario.nombre)
            put("correo", usuario.correo)
            put("tipo", usuario.tipo.valor)
            put("estado", usuario.estado)
        }
        return db.update("usuarios", values, "id = ?", arrayOf(usuario.id.toString()))
    }

    fun eliminar(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete("usuarios", "id = ?", arrayOf(id.toString()))
    }
}
