package com.example.cotacaofacil.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceEditModel(
    val cnpjBuyer : String = "",
    val code: String = "",
    var dateFinishPrice: Long? = 0,
    val productsEdit: MutableList<ProductPriceEditPriceModel> = mutableListOf()
) : Parcelable