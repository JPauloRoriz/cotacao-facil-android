package com.example.cotacaofacil.presentation.ui.extension

import java.text.SimpleDateFormat
import java.util.*

fun Long.formatDateHistoric() : String{
    return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt-br")).format(Date(this))
}

fun Long.toFormattedDateTime(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val date = Date(this)
    return dateFormat.format(date)
}

fun String.toDateTimeLong(): Long {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val date = dateFormat.parse(this)
    return date?.time ?: 0L
}