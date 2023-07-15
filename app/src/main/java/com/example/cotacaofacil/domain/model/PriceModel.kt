package com.example.cotacaofacil.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceModel(
    var code : String = "",
    var productsPrice : MutableList<ProductPriceModel> = mutableListOf(),
    val partnersAuthorized : MutableList<PartnerModel> = mutableListOf(),
    val dateStartPrice : Long = 0,
    val dateFinishPrice : Long? = 0,
    var priority : PriorityPrice = PriorityPrice.AVERAGE,
    var cnpjBuyerCreator : String = "",
    var closeAutomatic : Boolean = true,
    var allowAllProvider : Boolean = false,
    val deliveryDate : Long = 0,
    val description : String = "",
    var status : StatusPrice = StatusPrice.OPEN
) : Parcelable

enum class PriorityPrice{
    HIGH,
    AVERAGE,
    LOW
}

enum class StatusPrice{
    OPEN,
    CANCELED,
    FINISHED
}

