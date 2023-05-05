package com.example.cotacaofacil.presentation.viewmodel.buyer.home.contract

import com.example.cotacaofacil.domain.model.UserModel

sealed class HomeBuyerEvent{
    data class SuccessListProducts(val user : UserModel?) : HomeBuyerEvent()
    data class ListEmptyProducts(val user : UserModel?) : HomeBuyerEvent()
    object ErrorLoadListProducts : HomeBuyerEvent()

}
