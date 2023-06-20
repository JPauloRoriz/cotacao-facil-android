package com.example.cotacaofacil.presentation.viewmodel.product

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.domain.exception.EmptyFildException
import com.example.cotacaofacil.domain.exception.SaveDataEmptyConfirmationException
import com.example.cotacaofacil.domain.model.ProductModel
import com.example.cotacaofacil.domain.usecase.date.contract.DateCurrentUseCase
import com.example.cotacaofacil.domain.usecase.product.contract.DeleteProductUseCase
import com.example.cotacaofacil.domain.usecase.product.contract.EditProductUseCase
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
    private val editProductUseCase: EditProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val dateCurrentUseCase: DateCurrentUseCase,
    private val context: Context
) : ViewModel() {
    val stateLiveData = MutableLiveData(ProductAddState())
    val eventLiveData = SingleLiveEvent<ProductAddEvent>()

    init {
        eventLiveData.postValue(ProductAddEvent.GetListSpinner(getAllListSpinnerOptionsUseCase.invoke()))
    }

    fun createOrEdit(productModel: ProductModel?) {
        if (productModel != null) {
            stateLiveData.postValue(
                stateLiveData.value?.copy(
                    nameText = productModel.name,
                    descriptionText = productModel.description,
                    brandText = productModel.brand,
                    quantityText = productModel.quantity,
                    typeFilter = productModel.typeMeasurement,
                    titleBottomNavigation = context.getString(R.string.edit_product),
                    textButton = context.getString(R.string.edit_product),
                    isFavorite = productModel.isFavorite,
                    trashIsGone = false
                )
            )
        } else {
            stateLiveData.postValue(
                stateLiveData.value?.copy(
                    titleBottomNavigation = context.getString(R.string.add_product),
                    textButton = context.getString(R.string.save_product),
                    trashIsGone = true
                )
            )
        }
    }

    fun tapOnSaveProduct(
        name: String,
        description: String,
        brand: String,
        typeMeasurements: String,
        cnpjUser: String,
        quantity: String,
        isConfirmationDataEmpty: Boolean,
        isFavorite: Boolean,
        productModel: ProductModel?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (productModel == null) {
                dateCurrentUseCase.invoke().onSuccess { currentDate ->
                    saveProductionUseCase.invoke(
                        name,
                        description,
                        brand,
                        typeMeasurements,
                        cnpjUser,
                        quantity,
                        context,
                        isConfirmationDataEmpty,
                        isFavorite,
                        currentDate
                    )
                        .onSuccess {
                            eventLiveData.postValue(ProductAddEvent.ModificationProduct(context.getString(R.string.product_add_success)))
                        }.onFailure {
                            when (it) {
                                is SaveDataEmptyConfirmationException -> {
                                    eventLiveData.postValue(
                                        ProductAddEvent.ShowDialogConfirmationDataEmpty(
                                            name,
                                            description,
                                            brand,
                                            typeMeasurements,
                                            cnpjUser,
                                            quantity
                                        )
                                    )
                                }
                                is EmptyFildException -> {
                                    stateLiveData.postValue(stateLiveData.value?.copy(messageError = context.getString(R.string.name_is_mandatory)))
                                }
                                is ConnectException -> {
                                    stateLiveData.postValue(stateLiveData.value?.copy(messageError = context.getString(R.string.not_internet)))
                                }
                            }
                        }
                }
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    editProductUseCase.invoke(
                        ProductModel(
                            name,
                            description,
                            brand,
                            typeMeasurements,
                            cnpjUser,
                            productModel.code,
                            quantity,
                            isFavorite,
                            productModel.date
                        )
                    )
                        ?.onSuccess {
                            eventLiveData.postValue(ProductAddEvent.ModificationProduct(context.getString(R.string.edit_product)))
                        }
                        ?.onFailure {
                            when (it) {
                                is SaveDataEmptyConfirmationException -> {
                                    eventLiveData.postValue(
                                        ProductAddEvent.ShowDialogConfirmationDataEmpty(
                                            name,
                                            description,
                                            brand,
                                            typeMeasurements,
                                            cnpjUser,
                                            quantity
                                        )
                                    )
                                }
                                is EmptyFildException -> {
                                    stateLiveData.postValue(stateLiveData.value?.copy(messageError = context.getString(R.string.name_is_mandatory)))
                                }
                                is ConnectException -> {
                                    stateLiveData.postValue(stateLiveData.value?.copy(messageError = context.getString(R.string.not_internet)))
                                }
                            }
                        }
                }

            }
        }

    }

    fun tapOnIconFavorite(isFavorite: Boolean) {
        stateLiveData.postValue(stateLiveData.value?.copy(isFavorite = !isFavorite))
    }

    fun tapOnTrash(productModel: ProductModel?) {
        viewModelScope.launch(Dispatchers.IO) {
            productModel.let {
                deleteProductUseCase.invoke(it as ProductModel)
                    ?.onSuccess {
                        eventLiveData.postValue(ProductAddEvent.ModificationProduct(context.getString(R.string.product_deleted_success)))
                    }
                    ?.onFailure {
                        stateLiveData.postValue(stateLiveData.value?.copy(messageError = context.getString(R.string.impossible_delete_product)))
                    }
            }
        }

    }
}