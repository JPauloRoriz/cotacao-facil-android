package com.example.cotacaofacil.presentation.viewmodel.provider.price

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.domain.Extensions.Companion.convertCnpj
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.exception.ListEmptyException
import com.example.cotacaofacil.domain.exception.NoConnectionInternetException
import com.example.cotacaofacil.domain.model.PriceModel
import com.example.cotacaofacil.domain.model.StatusPrice
import com.example.cotacaofacil.domain.usecase.date.contract.DateCurrentUseCase
import com.example.cotacaofacil.domain.usecase.partner.contract.GetAllPartnerModelUseCase
import com.example.cotacaofacil.domain.usecase.price.contract.GetPricesProviderUseCase
import com.example.cotacaofacil.domain.usecase.price.contract.UpdateHourPricesUseCase
import com.example.cotacaofacil.domain.usecase.price.contract.ValidationPricesProviderUseCase
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.provider.price.contract.pricesProviderContract.PricePartnerEvent
import com.example.cotacaofacil.presentation.viewmodel.provider.price.contract.pricesProviderContract.PricePartnerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PriceProviderViewModel(
    val context: Context,
    val userHelper: UserHelper,
    private val getPricesProviderUseCase: GetPricesProviderUseCase,
    private val validationPricesProviderUseCase: ValidationPricesProviderUseCase,
    private val getAllPartnerModelUseCase: GetAllPartnerModelUseCase,
    private val updateHourPricesUseCase: UpdateHourPricesUseCase,
    private val dateCurrentUseCase: DateCurrentUseCase
) : ViewModel() {

    val stateLiveData = MutableLiveData(PricePartnerState())
    val eventLiveData = SingleLiveEvent<PricePartnerEvent>()
    private var allPrices: MutableList<PriceModel> = mutableListOf()

    init {
        userHelper.user?.cnpj?.let { cnpj ->
            eventLiveData.postValue(PricePartnerEvent.SendCnpjToAdapter(cnpjProvider = cnpj))
        }
        updateListPrices()
    }

    fun updateListPrices() {
        stateLiveData.postValue(stateLiveData.value?.copy(showProgressBar = true))
        viewModelScope.launch(Dispatchers.IO) {
            userHelper.user?.let { userModel ->
                userModel.id?.let {
                    getAllPartnerModelUseCase.invoke(userModel.userTypeSelected, it, userModel.cnpj)
                }?.onSuccess { partnersProvider ->
                    getPricesProviderUseCase.invoke(
                        partnersProvider.map { it.cnpjCorporation.convertCnpj() }.toMutableList(),
                        userModel.cnpj,
                        userModel
                    ).onSuccess { prices ->
                        if (prices.isEmpty()) {
                            stateLiveData.postValue(
                                stateLiveData.value?.copy(
                                    pricesModel = mutableListOf(),
                                    messageError = context.getString(R.string.price_empty_message_error),
                                    showProgressBar = false
                                )
                            )
                        } else {
                            dateCurrentUseCase.invoke().onSuccess { currentDate ->
                                allPrices = prices
                                stateLiveData.postValue(
                                    stateLiveData.value?.copy(
                                        pricesModel = validationPricesProviderUseCase.invoke(prices, userModel.cnpj, currentDate),
                                        messageError = "",
                                        showProgressBar = false
                                    )
                                )
                            }.onFailure {
                                stateLiveData.postValue(
                                    stateLiveData.value?.copy(
                                        pricesModel = mutableListOf(),
                                        messageError = context.getString(R.string.message_error_default_price),
                                        showProgressBar = false
                                    )
                                )
                            }
                        }
                    }
                        .onFailure { exception ->
                            stateLiveData.postValue(
                                stateLiveData.value?.copy(
                                    pricesModel = mutableListOf(),
                                    messageError = context.getString(R.string.message_error_default_price),
                                    showProgressBar = false
                                )
                            )
                            when (exception) {
                                is ListEmptyException -> {
                                    stateLiveData.postValue(
                                        stateLiveData.value?.copy(
                                            pricesModel = mutableListOf(),
                                            messageError = context.getString(R.string.price_empty_message_error),
                                            showProgressBar = false
                                        )
                                    )
                                }
                                is DefaultException -> {
                                    stateLiveData.postValue(
                                        stateLiveData.value?.copy(
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
                }?.onFailure {
                    stateLiveData.postValue(
                        stateLiveData.value?.copy(
                            pricesModel = mutableListOf(),
                            messageError = context.getString(R.string.message_error_default_price),
                            showProgressBar = false
                        )
                    )
                    //todo tratamento para internet
                }
            }
        }
    }

    fun tapOnPrice(priceModel: PriceModel) {
        updatePricesHour(priceModel)
        val priceUpdate = stateLiveData.value?.pricesModel?.find { it.code == priceModel.code }
        when (priceUpdate?.status) {
            StatusPrice.OPEN -> {
                userHelper.user?.cnpj?.let {
                    eventLiveData.postValue(PricePartnerEvent.TapOnPriceOpen(priceModel, it))
                }
            }
            StatusPrice.CANCELED -> {
                eventLiveData.postValue(PricePartnerEvent.TapOnPriceFinishedOrCanceled(priceModel))
            }
            StatusPrice.FINISHED -> {
                eventLiveData.postValue(PricePartnerEvent.TapOnPriceFinishedOrCanceled(priceModel))
            }
            else -> {
                stateLiveData.postValue(
                    stateLiveData.value?.copy(
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
            stateLiveData.value?.pricesModel?.let {
                val prices = it
                if (prices[prices.indexOf(priceModel)] != newPriceModel) {
                    prices[prices.indexOf(priceModel)] = newPriceModel
                    stateLiveData.postValue(stateLiveData.value?.copy(pricesModel = prices))
                }
            }
        }
    }

    fun filterPrices(statusPrice: StatusPrice?) {
        statusPrice?.let {
            stateLiveData.postValue(stateLiveData.value?.pricesModel?.filter { it.status == statusPrice }?.toMutableList()
                ?.let { stateLiveData.value?.copy(pricesModel = it) })
        } ?: stateLiveData.postValue(stateLiveData.value?.copy(pricesModel = allPrices))
    }

}