package com.example.cotacaofacil.presentation.viewmodel.price.model

import com.example.cotacaofacil.domain.model.ProductPriceEditPriceModel

data class PriceState(
    val isLoading: Boolean = false,
    val showBtnFinishPrice: Boolean = false,
    val showBtnCancelPrice: Boolean = false,
    val quantityProducts: String = "",
    val dateInit: String = "",
    val dateFinish: String = "",
    val quantityProviders: String = "",
    val productsPrice: MutableList<ProductPriceEditPriceModel> = mutableListOf()

)
