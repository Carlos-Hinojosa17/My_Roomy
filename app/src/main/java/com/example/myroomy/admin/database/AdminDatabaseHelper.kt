package com.example.myroomy.admin.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "myroomy.db", null, 3) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                correo TEXT NOT NULL UNIQUE,
                tipo TEXT NOT NULL,
                estado INTEGER DEFAULT 1
            )
        """)


        db.execSQL("""
            CREATE TABLE habitaciones (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            descripcion TEXT NOT NULL,
            categoria TEXT NOT NULL,
            imagen TEXT NOT NULL,
            estado TEXT NOT NULL,
            precio REAL NOT NULL,
            servicios TEXT NOT NULL,
            numeroHabitacion TEXT NOT NULL,
            piso INTEGER NOT NULL
        )
        """)

        db.execSQL("""
            CREATE TABLE reservas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                habitacion_id INTEGER NOT NULL,
                cliente_nombre TEXT NOT NULL,
                cliente_dni TEXT NOT NULL,
                fecha_inicio TEXT NOT NULL,
                fecha_fin TEXT NOT NULL,
                total REAL NOT NULL,
                FOREIGN KEY(habitacion_id) REFERENCES habitaciones(id)
            )
        """)



    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS habitaciones")
        db.execSQL("DROP TABLE IF EXISTS reservas")
        onCreate(db)
    }
}
