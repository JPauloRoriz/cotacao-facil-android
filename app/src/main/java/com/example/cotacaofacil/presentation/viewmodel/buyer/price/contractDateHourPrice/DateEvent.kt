package com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractDateHourPrice

import java.util.Calendar

sealed class DateEvent {
    data class SaveDate(val date : Long) : DateEvent()
    data class UpdateDate(val hour : Calendar) : DateEvent()
}