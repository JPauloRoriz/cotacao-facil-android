package com.example.cotacaofacil.presentation.viewmodel.provider.home

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
import com.example.cotacaofacil.domain.usecase.home.contract.GetBodyCompanyModelUseCase
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.provider.home.contract.HomeProviderEvent
import com.example.cotacaofacil.presentation.viewmodel.provider.home.contract.HomeProviderState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeProviderViewModel(
    private val userHelper: UserHelper,
    private val getBodyCompanyModelUseCase: GetBodyCompanyModelUseCase,
    private val sharedPreferences: SharedPreferencesHelper,
    private val context: Context,
    private val bodyCompanyHelper: BodyCompanyHelper
) : ViewModel() {

    val homeProviderEventLiveData = SingleLiveEvent<HomeProviderEvent>()
    val homeProviderStateLiveData = MutableLiveData(HomeProviderState())

    init {
        userHelper.user?.let { loadDataUser() }
    }

    fun loadDataUser() {
        val user = userHelper.user
        viewModelScope.launch(Dispatchers.IO) {
            user?.cnpj?.let {
                getBodyCompanyModelUseCase.invoke(it)
                    .onSuccess { bodyCompanyModel ->
                        homeProviderStateLiveData.postValue(
                            HomeProviderState(
                                false,
                                bodyCompanyModel.telefone.foneNotIsEmpty(context),
                                bodyCompanyModel.email.emailNotIsEmpty(context),
                                bodyCompanyModel.fantasia.nomeFantasyNotIsEmpty(context),
                                bodyCompanyModel.nome.nameCorporationNotIsEmpty(context),
                                user.cnpj.formatCnpj()
                            )
                        )
                    }
                    .onFailure {
                        when (it) {
                            is HttpException -> {
                                homeProviderStateLiveData.postValue(
                                    homeProviderStateLiveData.value?.copy(
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

    fun tapOnArrowBack(backPressedOnce: Boolean) {
        if (backPressedOnce) {
            homeProviderEventLiveData.postValue(HomeProviderEvent.FinishApp)
        } else {
            homeProviderEventLiveData.postValue(HomeProviderEvent.AskAgain)
        }
    }

    fun tapOnPartner() {
        homeProviderEventLiveData.postValue(HomeProviderEvent.ClickPartner)
    }

    fun tapOnLogout() {
        sharedPreferences.setStringSecret(sharedPreferences.KEY_USER_LOGIN, null, context)
        homeProviderEventLiveData.postValue(HomeProviderEvent.Logout)
    }
}