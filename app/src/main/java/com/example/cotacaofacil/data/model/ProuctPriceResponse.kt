package com.example.cotacaofacil.data.model

import android.os.Parcelable
import com.example.cotacaofacil.domain.model.ProductModel
import com.example.cotacaofacil.domain.model.UserPrice
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductPriceResponse(
    val productModel: ProductResponse = ProductResponse(),
    val usersPrice: MutableList<UserPrice> = mutableListOf(),
    var quantityProducts : Int = 1
) : Parcelable