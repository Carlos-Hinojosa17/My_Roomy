package com.example.myroomy.dashboard.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "myroomy.db", null, 7) { // ⚠️ Versión incrementada

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                correo TEXT NOT NULL UNIQUE,
                tipo TEXT NOT NULL,
                clave TEXT NOT NULL,
                estado INTEGER DEFAULT 1,
                urlFoto TEXT
            )
        """)

        // Usuarios predefinidos (solo tipos válidos: "admin" o "cliente")
        db.execSQL("""
            INSERT INTO usuarios (nombre, correo, tipo, clave, estado, urlFoto) VALUES
                ('Admin Principal', 'luisfr986@gmail.com', 'admin', 'ignxts', 1, '')
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
    INSERT INTO habitaciones (nombre, descripcion, categoria, imagen, estado, precio, servicios, numeroHabitacion, piso) VALUES
    ('Suite Ejecutiva', 'Suite con vista al mar y jacuzzi privado', 'Suite', 'ruta_suite1.jpg', 'Disponible', 350.0, 'Jacuzzi,TV,Wifi', '201', 2),
    ('Suite Deluxe', 'Suite de lujo con sala de estar', 'Suite', 'ruta_suite2.jpg', 'Ocupada', 400.0, 'TV,Wifi,Minibar', '202', 2),
    ('Habitación Doble A', 'Habitación doble estándar', 'Doble', 'ruta_doble1.jpg', 'Disponible', 180.0, 'TV,Wifi', '101', 1),
    ('Habitación Doble B', 'Habitación doble con balcón', 'Doble', 'ruta_doble2.jpg', 'Mantenimiento', 200.0, 'TV,Wifi,Balcón', '102', 1),
    ('Habitación Individual A', 'Habitación individual económica', 'Individual', 'ruta_individual1.jpg', 'Disponible', 120.0, 'Wifi', '301', 3),
    ('Habitación Individual B', 'Habitación individual con escritorio', 'Individual', 'ruta_individual2.jpg', 'Ocupada', 130.0, 'Wifi,Escritorio', '302', 3),
    ('Suite Presidencial', 'La mejor suite del hotel, piso alto', 'Suite', 'ruta_suite3.jpg', 'Disponible', 500.0, 'Jacuzzi,TV,Wifi,Minibar', '401', 4),
    ('Doble Familiar', 'Habitación doble ideal para familias', 'Doble', 'ruta_doble3.jpg', 'Disponible', 220.0, 'TV,Wifi,Cuna', '103', 1),
    ('Individual Compacta', 'Habitación compacta para viajeros', 'Individual', 'ruta_individual3.jpg', 'Mantenimiento', 110.0, 'Wifi', '303', 3)
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
