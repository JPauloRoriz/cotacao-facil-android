package com.example.cotacaofacil.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserPrice(
    private val cnpjProvider : String = "",
    private val price : Long = 0
) : Parcelable
