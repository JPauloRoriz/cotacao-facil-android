package com.example.cotacaofacil.presentation.viewmodel.buyer.home.contract

import com.example.cotacaofacil.domain.model.UserModel

sealed class HomeBuyerEvent{
    data class SuccessListProducts(val user : UserModel?) : HomeBuyerEvent()
    data class ListEmptyProducts(val user : UserModel?) : HomeBuyerEvent()
    data class ErrorLoadInformation(val message : String = "e") : HomeBuyerEvent()
    object ErrorLoadListProducts : HomeBuyerEvent()
    object ClickPartner : HomeBuyerEvent()
    object FinishApp : HomeBuyerEvent()
    object Logout : HomeBuyerEvent()
    object AskAgain : HomeBuyerEvent()
    object ClickCardPrices : HomeBuyerEvent()


}
