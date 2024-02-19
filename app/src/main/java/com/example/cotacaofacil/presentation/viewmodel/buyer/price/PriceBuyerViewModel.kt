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
import com.example.cotacaofacil.domain.model.PriceModel
import com.example.cotacaofacil.domain.model.StatusPrice
import com.example.cotacaofacil.domain.usecase.price.contract.GetPricesBuyerUserCase
import com.example.cotacaofacil.domain.usecase.price.contract.UpdateHourPricesUseCase
import com.example.cotacaofacil.domain.usecase.price.contract.ValidationStatusPriceUseCase
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractPrice.PriceEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractPrice.PriceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PriceBuyerViewModel(
    private val getPricesBuyerUserCase: GetPricesBuyerUserCase,
    private val validationStatusPriceUseCase: ValidationStatusPriceUseCase,
    private val updateHourPricesUseCase: UpdateHourPricesUseCase,
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
                userHelper.user?.let { userModel ->
                    getPricesBuyerUserCase.invoke(userModel.cnpj, userModel.userTypeSelected, userModel)
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
                                validationStatusPriceUseCase.invoke(prices).onSuccess {
                                    priceState.postValue(priceState.value?.copy(pricesModel = prices.sortedByDescending { it.status == StatusPrice.OPEN }.toMutableList(), messageError = "", showProgressBar = false))
                                }.onFailure {
                                    priceState.postValue(
                                        priceState.value?.copy(
                                            pricesModel = mutableListOf(),
                                            messageError = context.getString(R.string.message_error_default_price),
                                            showProgressBar = false
                                        )
                                    )
                                }
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

    fun tapOnPrice(priceModel: PriceModel) {
        updatePricesHour(priceModel)
        val priceUpdate = priceState.value?.pricesModel?.find { it.code == priceModel.code }
        when(priceUpdate?.status){
            StatusPrice.OPEN -> {
                priceEvent.postValue(PriceEvent.TapOnPriceOpen(priceModel))
            }
            StatusPrice.CANCELED -> {
                priceEvent.postValue(PriceEvent.TapOnPriceFinishedOrCanceled(priceModel))
            }
            StatusPrice.FINISHED -> {
                priceEvent.postValue(PriceEvent.TapOnPriceFinishedOrCanceled(priceModel))
            }
            else -> {
                priceState.postValue(
                    priceState.value?.copy(
                        pricesModel = mutableListOf(),
                        messageError = context.getString(R.string.message_error_not_find_price),
                        showProgressBar = false
                    )
                )
            }
        }
    }

    private fun updatePricesHour(priceModel: PriceModel) {
        viewModelScope.launch {
           val newPriceModel = updateHourPricesUseCase.invoke(priceModel)
            priceState.value?.pricesModel?.let {
                val prices = it
                if(prices[prices.indexOf(priceModel)] != newPriceModel){
                    prices[prices.indexOf(priceModel)] = newPriceModel
                    priceState.postValue(priceState.value?.copy(pricesModel = prices))
                }
            }
        }
    }
}