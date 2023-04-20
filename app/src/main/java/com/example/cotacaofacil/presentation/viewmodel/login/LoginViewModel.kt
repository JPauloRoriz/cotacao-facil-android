package com.example.cotacaofacil.presentation.viewmodel.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.exception.EmailOrPasswordInvalidException
import com.example.cotacaofacil.domain.exception.EmptyFildException
import com.example.cotacaofacil.domain.exception.NoConnectionInternetException
import com.example.cotacaofacil.domain.usecase.login.contract.LoginUseCase
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.login.model.LoginEvent
import com.example.cotacaofacil.presentation.viewmodel.login.model.LoginState
import kotlinx.coroutines.launch

class LoginViewModel(
    private val validationLoginUseCase: LoginUseCase,
    private val context: Context
) : ViewModel() {
    val stateLiveData = MutableLiveData(LoginState())
    val eventLiveData = SingleLiveEvent<LoginEvent>()


    fun tapOnRegister() {
        eventLiveData.value = LoginEvent.GoToRegister
    }

    suspend fun tapOnLogin(email: String, password: String) {
        viewModelScope.launch {
            stateLiveData.setLoadingLogin(true)
            validationLoginUseCase.invoke(email, password)
                .onSuccess { user ->
                    stateLiveData.setLoadingLogin(false)
                    stateLiveData.setMessageErrorLogin("")
                    if (user.userTypeSelected.userProviderSelected) {
                        eventLiveData.value = LoginEvent.SuccessLoginProvider(user)
                    } else {
                        eventLiveData.value = LoginEvent.SuccessLoginBuyer(user)
                    }

                }.onFailure { error ->
                    when (error) {
                        is EmailOrPasswordInvalidException -> {
                            stateLiveData.setMessageErrorLogin(context.getString(R.string.email_or_password_invald))
                        }
                        is NoConnectionInternetException -> {
                            stateLiveData.setMessageErrorLogin(context.getString(R.string.not_internet))
                        }
                        is EmptyFildException -> {
                            stateLiveData.setMessageErrorLogin(context.getString(R.string.empty_fild))
                        }
                        is DefaultException -> {
                            stateLiveData.setMessageErrorLogin(context.getString(R.string.login_not_realized))
                        }
                        else -> {
                            stateLiveData.setMessageErrorLogin(context.getString(R.string.login_not_realized))
                        }
                    }

                }
        }
    }

    private fun MutableLiveData<LoginState>.setLoadingLogin(value: Boolean) {
        this.value = this.value?.copy(isLoading = value)
    }

    private fun MutableLiveData<LoginState>.setMessageErrorLogin(message: String) {
        this.value = this.value?.copy(isLoading = false, messageError = message)
    }


}