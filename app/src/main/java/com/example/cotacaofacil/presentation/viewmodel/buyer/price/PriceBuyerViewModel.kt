package com.example.cotacaofacil.presentation.viewmodel.buyer.price

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.exception.ListEmptyException
import com.example.cotacaofacil.domain.exception.NoConnectionInternetException
import com.example.cotacaofacil.domain.usecase.price.contract.GetPricesBuyerUserCase
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractPrice.PriceEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractPrice.PriceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PriceBuyerViewModel(
    private val getPricesBuyerUserCase: GetPricesBuyerUserCase,
    private val userHelper: UserHelper,
    private val context: Context,
) : ViewModel() {
    val priceEvent = SingleLiveEvent<PriceEvent>()
    val priceState = MutableLiveData(PriceState())

    init {
        updateListPrices()
    }

    fun updateListPrices() {
        priceState.postValue(priceState.value?.copy(showProgressBar = true))
        viewModelScope.launch(Dispatchers.IO) {
            userHelper.user?.cnpj?.let { cnpj ->
                getPricesBuyerUserCase.invoke(cnpj)
                    .onSuccess { prices ->
                        if (prices.isEmpty()) {
                            priceState.postValue(
                                priceState.value?.copy(
                                    pricesModel = mutableListOf(),
                                    messageError = context.getString(R.string.price_empty_message_error),
                                    showProgressBar = false
                                )
                            )
                        } else {
                            priceState.postValue(priceState.value?.copy(pricesModel = prices, messageError = "", showProgressBar = false))
                        }
                    }
                    .onFailure { exception ->
                        when (exception) {
                            is ListEmptyException -> {
                                priceState.postValue(
                                    priceState.value?.copy(
                                        pricesModel = mutableListOf(),
                                        messageError = context.getString(R.string.price_empty_message_error),
                                        showProgressBar = false
                                    )
                                )
                            }
                            is DefaultException -> {
                                priceState.postValue(
                                    priceState.value?.copy(
                                        pricesModel = mutableListOf(),
                                        messageError = context.getString(R.string.message_error_default_price),
                                        showProgressBar = false
                                    )
                                )
                            }
                            is NoConnectionInternetException -> {
                                //todo tratamento para internet
                            }
                        }
                    }
            }
        }
    }

    fun tapOnCreatePrice() {
        priceEvent.postValue(PriceEvent.CreatePrice)
    }

    fun showDialogSuccess(it: String) {
        priceEvent.postValue(PriceEvent.ShowDialogSuccess(it))
    }
}