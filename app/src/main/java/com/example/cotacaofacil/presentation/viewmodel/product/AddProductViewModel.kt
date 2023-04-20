package com.example.cotacaofacil.presentation.viewmodel.product

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.domain.exception.EmptyFildException
import com.example.cotacaofacil.domain.exception.SaveDataEmptyConfirmationException
import com.example.cotacaofacil.domain.usecase.product.contract.GetAllListSpinnerOptionsUseCase
import com.example.cotacaofacil.domain.usecase.product.contract.SaveProductionUseCase
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.product.model.ProductAddEvent
import com.example.cotacaofacil.presentation.viewmodel.product.model.ProductAddState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ConnectException

class AddProductViewModel(
    private val getAllListSpinnerOptionsUseCase: GetAllListSpinnerOptionsUseCase,
    private val saveProductionUseCase: SaveProductionUseCase,
    private val context: Context
) : ViewModel() {
    val stateLiveData = MutableLiveData(ProductAddState())
    val eventLiveData = SingleLiveEvent<ProductAddEvent>()

    init {
        eventLiveData.postValue(ProductAddEvent.GetListSpinner(getAllListSpinnerOptionsUseCase.invoke()))
    }

    fun tapOnSaveProduct(name: String, description: String, brand: String, typeMeasurements: String, cnpjUser: String, quantity: String, isConfirmationDataEmpty : Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            saveProductionUseCase.invoke(name, description, brand, typeMeasurements, cnpjUser, quantity ,context, isConfirmationDataEmpty)
                .onSuccess {
                    eventLiveData.postValue(ProductAddEvent.SuccessAddProductAdd)
                }.onFailure {
                    when (it) {
                        is SaveDataEmptyConfirmationException -> {
                            eventLiveData.postValue(ProductAddEvent.ShowDialogConfirmationDataEmpty(
                                name,
                                description,
                                brand,
                                typeMeasurements,
                                cnpjUser,
                                quantity
                            ))
                        }
                        is EmptyFildException -> {
                            stateLiveData.postValue(ProductAddState().copy(messageError = context.getString(R.string.name_is_mandatory)))
                        }
                        is ConnectException -> {
                            stateLiveData.postValue(ProductAddState().copy(messageError = context.getString(R.string.not_internet)))
                        }
                    }
                }

        }
    }

}