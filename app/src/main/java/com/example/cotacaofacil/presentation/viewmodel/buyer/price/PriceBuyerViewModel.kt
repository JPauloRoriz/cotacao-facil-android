package com.example.cotacaofacil.presentation.viewmodel.buyer.price

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractPrice.PriceEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractPrice.PriceState

class PriceBuyerViewModel : ViewModel() {
    val priceEvent = SingleLiveEvent<PriceEvent>()
    val priceState = MutableLiveData<PriceState>()

    init {

    }

    fun tapOnCreatePrice(){
        priceEvent.postValue(PriceEvent.CreatePrice)
    }
}