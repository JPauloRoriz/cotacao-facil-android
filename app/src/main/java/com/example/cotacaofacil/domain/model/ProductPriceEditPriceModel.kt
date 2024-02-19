package com.example.cotacaofacil.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductPriceEditPriceModel(
    var productModel: ProductModel = ProductModel(),
    var price: Double = 0.0,
    var isSelected: Boolean = false,
    var quantityProducts: Int = 1
) : Parcelable