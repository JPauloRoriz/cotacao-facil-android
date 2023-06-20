package com.example.cotacaofacil.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PartnerResponse(
    var cnpjUser : String = "",
    var approved : Boolean = false,
    var date : Long = 0,
    var cnpjRequestingUser : String = ""
): Parcelable
