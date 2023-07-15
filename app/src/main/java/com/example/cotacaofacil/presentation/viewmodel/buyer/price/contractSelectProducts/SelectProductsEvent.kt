package com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractSelectProducts

import com.example.cotacaofacil.domain.model.PriceModel
import com.example.cotacaofacil.domain.model.ProductPriceModel

sealed class SelectProductsEvent{
    object UpdateList : SelectProductsEvent()
    data class Next(val priceModel : PriceModel) : SelectProductsEvent()
    data class ErrorSelectMinOneProduct(val message : String) : SelectProductsEvent()
    data class UpdateListProducts(val products : MutableList<ProductPriceModel>) : SelectProductsEvent()

}
