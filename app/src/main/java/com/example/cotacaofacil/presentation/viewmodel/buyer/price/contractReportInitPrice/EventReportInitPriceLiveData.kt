package com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractReportInitPrice

import com.example.cotacaofacil.domain.model.ProductPriceEditPriceModel

sealed class EventReportInitPriceLiveData {
    data class UpdateListProducts(val productPriceModelList: MutableList<ProductPriceEditPriceModel>) : EventReportInitPriceLiveData()
    data class SuccessAddPrice(val codePrice: String) : EventReportInitPriceLiveData()
}
