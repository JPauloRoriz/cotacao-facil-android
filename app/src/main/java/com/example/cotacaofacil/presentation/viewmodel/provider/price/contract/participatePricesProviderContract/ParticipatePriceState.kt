package com.example.cotacaofacil.presentation.viewmodel.provider.price.contract.participatePricesProviderContract

import com.example.cotacaofacil.domain.model.ProductPriceEditPriceModel

data class ParticipatePriceState(
    var isLoading: Boolean = false,
    var productsPricesModel: MutableList<ProductPriceEditPriceModel> = mutableListOf(),
    var showError: Boolean = false,
    var textViewCodePrice: String = "",
    var valueTotal: String = "",
    var productEditSelect: ProductPriceEditPriceModel = ProductPriceEditPriceModel(),
)
