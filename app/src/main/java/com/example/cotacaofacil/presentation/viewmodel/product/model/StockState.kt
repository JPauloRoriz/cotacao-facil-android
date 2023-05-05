package com.example.cotacaofacil.presentation.viewmodel.product.model

import android.opengl.Visibility
import android.view.View
import com.example.cotacaofacil.domain.model.ProductModel

data class StockState (
    val messageError: String = "",
    val isLoading: Boolean = false,
    val productsList: MutableList<ProductModel> = arrayListOf(),
    val showListProducts: Int = View.VISIBLE,
    val showError: Int = View.GONE,

)