package com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractSelectProducts

sealed class SelectProductsEvent{
    object UpdateList : SelectProductsEvent()
    object Next : SelectProductsEvent()
    data class ErrorSelectMinOneProduct(val message : String) : SelectProductsEvent()

}
