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
import com.example.cotacaofacil.domain.model.BodyCompanyModel
import com.example.cotacaofacil.domain.model.ProductModel
import com.example.cotacaofacil.domain.model.StatusPrice
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.domain.usecase.home.contract.GetBodyCompanyModelUseCase
import com.example.cotacaofacil.domain.usecase.price.contract.GetPricesBuyerUserCase
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
    private val bodyCompanyHelper: BodyCompanyHelper,
    private val getPricesBuyerUserCase: GetPricesBuyerUserCase
) : ViewModel() {

    val homeBuyerEventLiveData = SingleLiveEvent<HomeBuyerEvent>()
    val homeBuyerStateLiveData = MutableLiveData(HomeBuyerState())

    init {
        userHelper.user?.let { user ->
            loadDataUser()
            loadDataPrice(user)
        }
    }

    private fun loadDataPrice(user: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            getPricesBuyerUserCase.invoke(cnpjUser = user.cnpj, userTypeSelected = user.userTypeSelected, userModel = user)
                .onSuccess { prices ->
                    val pricesStatusOpen = prices.filter { it.status == StatusPrice.OPEN }
                    homeBuyerStateLiveData.postValue(
                        homeBuyerStateLiveData.value?.copy(
                            quantityPrice = pricesStatusOpen.size.toString()
                        )
                    )

                }
                .onFailure {

                }
        }
    }

    fun loadDataUser() {
        val user = userHelper.user
        viewModelScope.launch(Dispatchers.IO) {
            user?.cnpj?.let {
                getBodyCompanyModelUseCase.invoke(it)
                    .onSuccess { bodyCompanyModel ->
                        handleSuccessBodyCompany(bodyCompanyModel, user)
                    }
                    .onFailure { exception ->
                        when (exception) {
                            is HttpException -> {
                                handlerErrorHttp(user)
                            }
                        }
                    }
            }
        }
    }

    private fun handlerErrorHttp(user: UserModel) {
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

    private fun handleSuccessBodyCompany(bodyCompanyModel: BodyCompanyModel, user: UserModel) {
        viewModelScope.launch {
            getAllProductsUseCase.invoke(user.cnpj)
                .onSuccess { productsModelList ->
                    successGetProducts(bodyCompanyModel, user, productsModelList)
                }.onFailure {
                    //todo tratamento de erros
                }
        }
    }

    private fun successGetProducts(bodyCompanyModel: BodyCompanyModel, user: UserModel, productsModelList: MutableList<ProductModel>) {
        bodyCompanyModel.apply {
            homeBuyerStateLiveData.postValue(
                homeBuyerStateLiveData.value?.copy(
                    isLoading = false,
                    fone = telefone.foneNotIsEmpty(context),
                    email = email.emailNotIsEmpty(context),
                    nameFantasy = fantasia.nomeFantasyNotIsEmpty(context),
                    nameCorporation = nome.nameCorporationNotIsEmpty(context),
                    cnpj = user.cnpj.formatCnpj(),
                    quantityProducts = productsModelList.size.toString()
                )
            )
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

    fun tapOnCardPrices() {
        homeBuyerEventLiveData.postValue(HomeBuyerEvent.ClickCardPrices)
    }
}
