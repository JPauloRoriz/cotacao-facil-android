package com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractDateHourPrice

import java.util.*

data class DateState(
    var messageError: String = "",
    val date: Calendar = Calendar.getInstance(),
    val isNight: Boolean = false,
)
