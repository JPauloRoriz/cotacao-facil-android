package com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractSelectProducts

import com.example.cotacaofacil.R
import com.example.cotacaofacil.domain.model.ProductPriceModel

data class SelectProductsState(
    var messageError : String = "",
    val products : MutableList<ProductPriceModel> = mutableListOf(),
    val showMessageError : Boolean = false,
    var isLoading : Boolean = true,
    var colorButtonNext : Int = R.color.gray,
    var colorTextButtonNext : Int = R.color.black,
)
