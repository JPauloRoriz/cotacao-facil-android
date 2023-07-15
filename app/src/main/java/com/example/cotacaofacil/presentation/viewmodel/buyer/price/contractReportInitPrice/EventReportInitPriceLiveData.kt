package com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractReportInitPrice

import com.example.cotacaofacil.domain.model.ProductPriceModel

sealed class EventReportInitPriceLiveData {
    data class UpdateListProducts(val productPriceModelList: MutableList<ProductPriceModel>) : EventReportInitPriceLiveData()
    data class SuccessAddPrice(val codePrice: String) : EventReportInitPriceLiveData()
}
