package com.example.cotacaofacil.presentation.viewmodel.product

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.exception.ListEmptyException
import com.example.cotacaofacil.domain.usecase.product.contract.GetAllProductsUseCase
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.product.model.StockEvent
import com.example.cotacaofacil.presentation.viewmodel.product.model.StockState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StockBuyerViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val userHelper: UserHelper,
    private val context: Context
) : ViewModel() {

    val stateLiveData = MutableLiveData(StockState())
    val eventLiveData = SingleLiveEvent<StockEvent>()

    private val user = userHelper.user
    init {
        initViewModel()
    }

    fun initViewModel() {
        viewModelScope.launch(Dispatchers.IO) {
            stateLiveData.postValue(StockState().copy(isLoading = true))
            user?.cnpj?.let {
                getAllProductsUseCase.invoke(it)
                    .onSuccess {
                        stateLiveData.postValue(
                            StockState().copy(
                                productsList = it,
                                messageError = "",
                                isLoading = false,
                                showError = View.GONE,
                                showListProducts = View.VISIBLE
                            )
                        )
                        eventLiveData.postValue(StockEvent.UpdateList(it))
                    }
                    .onFailure { exception ->
                        when (exception) {
                            is ListEmptyException -> {
                                stateLiveData.postValue(StockState().copy(
                                    isLoading = false,
                                    showError = View.VISIBLE
                                ))
                                eventLiveData.postValue(StockEvent.StockEmptyEvent)
                            }
                            is DefaultException -> {
                                stateLiveData.postValue(
                                    StockState().copy(
                                        messageError = context.getString(R.string.message_error_impossible_load_products_stock),
                                        showListProducts = View.GONE,
                                        showError = View.VISIBLE,
                                        isLoading = false
                                    )
                                )
                            }

                        }
                    }
            }
        }
    }

}
