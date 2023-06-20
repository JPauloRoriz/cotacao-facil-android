package com.example.cotacaofacil.presentation.viewmodel.buyer.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.data.helper.BodyCompanyHelper
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.data.sharedPreferences.SharedPreferencesHelper
import com.example.cotacaofacil.domain.Extensions.Companion.emailNotIsEmpty
import com.example.cotacaofacil.domain.Extensions.Companion.foneNotIsEmpty
import com.example.cotacaofacil.domain.Extensions.Companion.formatCnpj
import com.example.cotacaofacil.domain.Extensions.Companion.nameCorporationNotIsEmpty
import com.example.cotacaofacil.domain.Extensions.Companion.nomeFantasyNotIsEmpty
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.exception.ListEmptyException
import com.example.cotacaofacil.domain.usecase.home.contract.GetBodyCompanyModelUseCase
import com.example.cotacaofacil.domain.usecase.product.contract.GetAllByCnpjProductsUseCase
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.home.contract.HomeBuyerEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.home.contract.HomeBuyerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeBuyerViewModel(
    private val getAllProductsUseCase: GetAllByCnpjProductsUseCase,
    private val userHelper: UserHelper,
    private val getBodyCompanyModelUseCase: GetBodyCompanyModelUseCase,
    private val sharedPreferences: SharedPreferencesHelper,
    private val context: Context,
    private val bodyCompanyHelper: BodyCompanyHelper
) : ViewModel() {

    val homeBuyerEventLiveData = SingleLiveEvent<HomeBuyerEvent>()
    val homeBuyerStateLiveData = MutableLiveData(HomeBuyerState())

    init {
        userHelper.user?.let { loadDataUser() }
    }

    fun loadDataUser() {
        val user = userHelper.user
        viewModelScope.launch(Dispatchers.IO) {
            user?.cnpj?.let {
                getBodyCompanyModelUseCase.invoke(it)
                    .onSuccess { bodyCompanyModel ->
                        getAllProductsUseCase.invoke(user.cnpj)
                            .onSuccess {productsModelList ->
                                homeBuyerStateLiveData.postValue(
                                    homeBuyerStateLiveData.value?.copy(
                                        false,
                                        bodyCompanyModel.telefone.foneNotIsEmpty(context),
                                        bodyCompanyModel.email.emailNotIsEmpty(context),
                                        bodyCompanyModel.fantasia.nomeFantasyNotIsEmpty(context),
                                        bodyCompanyModel.nome.nameCorporationNotIsEmpty(context),
                                        user.cnpj.formatCnpj(),
                                        quantityProducts = productsModelList.size.toString()
                                    )
                                )
                            }.onFailure {
                                //todo tratamento de erros
                            }

                    }
                    .onFailure {
                        when (it) {
                            is HttpException -> {
//                                homeBuyerEventLiveData.postValue(
//                                    HomeBuyerEvent.ErrorLoadInformation(context.getString(R.string.error_http_4229))
//                                )
                                homeBuyerStateLiveData.postValue(
                                    homeBuyerStateLiveData.value?.copy(
                                        isLoading = false,
                                        fone = bodyCompanyHelper.bodyCompany?.telefone.foneNotIsEmpty(context),
                                        email = bodyCompanyHelper.bodyCompany?.email.emailNotIsEmpty(context),
                                        nameFantasy = bodyCompanyHelper.bodyCompany?.fantasia.nomeFantasyNotIsEmpty(context),
                                        nameCorporation = bodyCompanyHelper.bodyCompany?.nome.nameCorporationNotIsEmpty(context),
                                        cnpj = user.cnpj.formatCnpj()
                                    )
                                )

                            }
                        }
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
