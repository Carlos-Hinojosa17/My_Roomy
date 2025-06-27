package com.example.myroomy.admin.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Usuario(
    val id: Int,
    val nombre: String,
    val correo: String,
    val tipo: TipoUsuario,
    val estado: Int
) : Parcelable
