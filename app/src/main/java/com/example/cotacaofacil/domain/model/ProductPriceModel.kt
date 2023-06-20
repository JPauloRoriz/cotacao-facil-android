package com.example.cotacaofacil.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductPriceModel(
     val productModel: ProductModel = ProductModel(),
     val usersPrice: MutableList<UserPrice> = mutableListOf(),
     var isSelected : Boolean = false
) : Parcelable