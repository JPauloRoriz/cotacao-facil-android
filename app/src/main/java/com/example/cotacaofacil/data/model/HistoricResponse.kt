package com.example.cotacaofacil.data.model

import android.os.Parcelable
import com.example.cotacaofacil.domain.model.TypeHistory
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoricResponse(
    var type : TypeHistory = TypeHistory.NEW_PARTNER_ADD,
    var date : Long = 0L,
    var nameAssistant: String = ""
) : Parcelable
