package com.example.cotacaofacil.presentation.viewmodel.buyer.home

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.data.helper.BodyCompanyHelper
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.data.sharedPreferences.SharedPreferencesHelper
import com.example.cotacaofacil.domain.Extensions.Companion.formatCnpj
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.exception.ListEmptyException
import com.example.cotacaofacil.domain.usecase.home.contract.GetBodyCompanyModelUseCase
import com.example.cotacaofacil.domain.usecase.product.contract.GetAllProductsUseCase
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.home.model.HomeBuyerEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.home.model.HomeBuyerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeBuyerViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val userHelper: UserHelper,
    private val getBodyCompanyModelUseCase: GetBodyCompanyModelUseCase,
    private val sharedPreferences: SharedPreferencesHelper,
    private val context: Context,
    private val bodyCompanyHelper: BodyCompanyHelper
) : ViewModel() {

    val homeBuyerEventLiveData = SingleLiveEvent<HomeBuyerEvent>()
    val homeBuyerStateLiveData = MutableLiveData<HomeBuyerState>()

    init {
        userHelper.user?.let { loadDataUser() }
    }

    fun loadDataUser() {
        val user = userHelper.user
        viewModelScope.launch(Dispatchers.IO) {
            user?.cnpj?.let {
                getBodyCompanyModelUseCase.invoke(it)
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
                                            nameCorporation,
                                            user.cnpj.formatCnpj()
                                        )
                                    }
                                }
                            }
                        })
                        bodyCompanyHelper.bodyCompany?.email = homeBuyerStateLiveData.value?.email?:context.getString(R.string.email_null)
                        bodyCompanyHelper.bodyCompany?.nome = homeBuyerStateLiveData.value?.nameCorporation?:context.getString(R.string.name_corporation_null)
                        bodyCompanyHelper.bodyCompany?.fantasia = homeBuyerStateLiveData.value?.nameFantasy?:context.getString(R.string.fantasy_null)
                        bodyCompanyHelper.bodyCompany?.telefone = homeBuyerStateLiveData.value?.fone?:context.getString(R.string.fone_null)
                    }
                    .onFailure {
                        when (it) {
                            is HttpException -> {
//                                homeBuyerEventLiveData.postValue(
//                                    HomeBuyerEvent.ErrorLoadInformation(context.getString(R.string.error_http_4229))
//                                )
                                homeBuyerStateLiveData.postValue(
                                    bodyCompanyHelper.bodyCompany?.telefone?.let { fone ->
                                        bodyCompanyHelper.bodyCompany?.email?.let { email ->
                                            bodyCompanyHelper.bodyCompany?.fantasia?.let { nameFantasy ->
                                                bodyCompanyHelper.bodyCompany?.nome?.let { nameCorporation ->
                                                    homeBuyerStateLiveData.value?.copy(
                                                        fone = fone,
                                                        nameFantasy = nameFantasy,
                                                        nameCorporation = nameCorporation,
                                                        cnpj = user.cnpj,
                                                        email = email
                                                    )
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }

                    }
                getAllProductsUseCase.invoke(user.cnpj)
                    .onSuccess {
                        homeBuyerStateLiveData.postValue(homeBuyerStateLiveData.value?.copy(quantityProducts = it.size.toString()))
                    }.onFailure {

                    }
            }
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

    fun tapOnArrowBack(backPressedOnce: Boolean) {
        if (backPressedOnce) {
            homeBuyerEventLiveData.postValue(HomeBuyerEvent.FinishApp)
        } else {
            homeBuyerEventLiveData.postValue(HomeBuyerEvent.AskAgain)
        }
    }

    fun tapOnPartner() {
        homeBuyerEventLiveData.postValue(HomeBuyerEvent.ClickPartner)
    }

    fun tapOnLogout() {
        sharedPreferences.setStringSecret(sharedPreferences.KEY_USER_LOGIN, null, context)
        homeBuyerEventLiveData.postValue(HomeBuyerEvent.Logout)
    }
}