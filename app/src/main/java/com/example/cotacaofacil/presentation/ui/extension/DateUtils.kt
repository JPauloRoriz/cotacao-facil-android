package com.example.cotacaofacil.presentation.ui.extension

import java.text.SimpleDateFormat
import java.util.*

fun Long.formatDateHistoric() : String{
    return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt-br")).format(Date(this))
}