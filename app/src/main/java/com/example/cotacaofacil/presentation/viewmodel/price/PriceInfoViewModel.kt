package com.example.cotacaofacil.presentation.viewmodel.price

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.domain.Extensions.Companion.getCnpjProviders
import com.example.cotacaofacil.domain.mapper.toPriceEditModel
import com.example.cotacaofacil.domain.model.PriceModel
import com.example.cotacaofacil.domain.model.StatusPrice
import com.example.cotacaofacil.domain.usecase.date.contract.DateCurrentUseCase
import com.example.cotacaofacil.domain.usecase.price.contract.EditPriceUseCase
import com.example.cotacaofacil.domain.usecase.price.contract.GetPriceByCodeUseCase
import com.example.cotacaofacil.presentation.ui.extension.toFormattedDateTime
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.price.model.PriceEvent
import com.example.cotacaofacil.presentation.viewmodel.price.model.PriceState
import kotlinx.coroutines.launch

class PriceInfoViewModel(
    private val getPriceByCodeUseCase: GetPriceByCodeUseCase,
    private val codePrice: String,
    private val cnpjBuyerCreator: String,
    private val context: Context,
    private val editPriceUseCase: EditPriceUseCase,
    private val currentUseCase: DateCurrentUseCase,

    ) : ViewModel() {
    val stateLiveData = MutableLiveData(PriceState(isLoading = true))
    val eventLiveData = SingleLiveEvent<PriceEvent>()
    var priceModelInit: PriceModel? = null

    init {
        getPriceModel()
    }

    private fun getPriceModel() {
        viewModelScope.launch {
            getPriceByCodeUseCase.invoke(codePrice = codePrice, cnpjBuyerCreator = cnpjBuyerCreator)
                .onSuccess { priceModel ->
                    priceModelInit = priceModel
                    getDataState(priceModel)

                }
                .onFailure {

                }
        }
    }

    private fun getDataState(priceModel: PriceModel) {
        var showBtnFinish = false
        var showBtnCancel = false
        var textFinishPrice = ""
        when (priceModel.status) {
            StatusPrice.OPEN -> {
                if (priceModel.closeAutomatic.not()) {
                    showBtnFinish = true
                    textFinishPrice = context.getString(R.string.price_finish_date, priceModel.dateFinishPrice?.toFormattedDateTime())
                } else {
                    textFinishPrice = context.getString(R.string.finish_price_manual)
                }
                showBtnCancel = true
            }
            StatusPrice.CANCELED -> {
                textFinishPrice = context.getString(R.string.price_canceled_date, priceModel.dateFinishPrice?.toFormattedDateTime())
            }
            StatusPrice.FINISHED -> {
                textFinishPrice = context.getString(R.string.price_finished_date, priceModel.dateFinishPrice?.toFormattedDateTime())
            }
        }
        stateLiveData.postValue(
            stateLiveData.value?.copy(
                showBtnFinishPrice = showBtnFinish,
                showBtnCancelPrice = showBtnCancel,
                isLoading = false,
                productsPrice = priceModel.toPriceEditModel("").productsEdit,
                quantityProducts = priceModel.productsPrice.size.toString(),
                dateInit = context.getString(R.string.date_init_price_adapter_price, priceModel.dateStartPrice.toFormattedDateTime()),
                dateFinish = textFinishPrice,
                quantityProviders = priceModel.getCnpjProviders().size.toString()
            )
        )
    }

    fun tapOnCancelPrice() {
        priceModelInit?.let { priceModel ->
            viewModelScope.launch {
                currentUseCase.invoke()
                    .onSuccess { date ->
                        priceModelInit?.apply {
                            status = StatusPrice.CANCELED
                            dateFinishPrice = date
                        }
                        editPriceUseCase.invoke(priceModel)
                    }
                    .onSuccess {

                    }.onFailure {
                        //todo tratamento de erro

                    }.onFailure {
                        //todo tratamento de erro
                    }
            }
        }
    }
}