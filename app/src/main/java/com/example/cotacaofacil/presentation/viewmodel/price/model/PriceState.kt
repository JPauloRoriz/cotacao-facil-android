package com.example.cotacaofacil.presentation.viewmodel.price.model

import com.example.cotacaofacil.domain.model.ProductPriceEditPriceModel

data class PriceState(
    val isLoading: Boolean = false,
    val showBtnFinishPrice: Boolean = true,
    val showBtnCancelPrice: Boolean = true,
    val quantityProducts: String = "",
    val dateInit: String = "",
    val dateFinish: String = "",
    val quantityProviders: String = "",
    val productsPrice: MutableList<ProductPriceEditPriceModel> = mutableListOf()

)
