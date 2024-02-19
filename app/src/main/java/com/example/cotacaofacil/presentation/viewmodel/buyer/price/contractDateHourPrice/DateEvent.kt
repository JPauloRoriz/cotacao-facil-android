package com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractDateHourPrice

import java.util.*

sealed class DateEvent {
    data class SaveDate(val date : Long) : DateEvent()
    data class UpdateDate(val hour: Date) : DateEvent()
}