package com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractDateHourPrice

import java.util.*

data class DateState(
    var messageError: String = "",
    val date: Long = Date().time,
    val periodDay: TimeOfDay = TimeOfDay.MORNING,
)

enum class TimeOfDay {
    NIGHT,
    MORNING,
    AFTERNOON
}
