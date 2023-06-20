package com.example.cotacaofacil.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceModel(
    val code : String = "",
    val productSPrice : MutableList<ProductPriceModel> = mutableListOf(),
    val partnersAuthorized : MutableList<PartnerModel> = mutableListOf(),
    val dateStartPrice : Long = 0,
    val dateFinishPrice : Long? = null,
    val priority : PriorityPrice = PriorityPrice.AVERAGE,
    val cnpjBuyerCreator : String = "",
    val isCloseAutomatic : Boolean = true,
    val allowAllProvider : Boolean = false,
    val deliveryDate : Long = 0,
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

