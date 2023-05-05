package com.example.cotacaofacil.presentation.viewmodel.buyer.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.exception.ListEmptyException
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.domain.usecase.home.contract.GetBodyCompanyModelUseCase
import com.example.cotacaofacil.domain.usecase.product.contract.GetAllProductsUseCase
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.home.contract.HomeBuyerEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.home.contract.HomeBuyerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeBuyerViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val userHelper: UserHelper,
    private val getBodyCompanyModelUseCase: GetBodyCompanyModelUseCase
) : ViewModel() {

    val homeBuyerEventLiveData = SingleLiveEvent<HomeBuyerEvent>()
    val homeBuyerStateLiveData = MutableLiveData<HomeBuyerState>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userHelper.user?.let { loadDataUser(it) }
        }
    }

    private suspend fun loadDataUser(user: UserModel) {
        getBodyCompanyModelUseCase.invoke(user.cnpj)
            .onSuccess {
                homeBuyerStateLiveData.postValue(it.fantasia?.let { nameFantasy ->
                    it.nome?.let { nameCorporation ->
                        it.telefone?.let { fone ->
                            it.email?.let { email ->
                                HomeBuyerState(
                                    false,
                                    fone,
                                    email,
                                    nameFantasy,
                                    nameCorporation
                                )
                            }
                        }
                    }
                })
            }
            .onFailure {

            }
    }

    fun tapOnCardStock() {
        viewModelScope.launch(Dispatchers.IO) {
            userHelper.user?.cnpj?.let {
                getAllProductsUseCase.invoke(it)
                    .onSuccess {
                        homeBuyerEventLiveData.postValue(HomeBuyerEvent.SuccessListProducts(userHelper.user))
                    }
                    .onFailure { exception ->
                        when (exception) {
                            is ListEmptyException -> {
                                homeBuyerEventLiveData.postValue(HomeBuyerEvent.ListEmptyProducts(userHelper.user))
                            }
                            is DefaultException -> {
                                homeBuyerEventLiveData.postValue(HomeBuyerEvent.ErrorLoadListProducts)
                            }

                        }
                    }
            }

        }
    }

}