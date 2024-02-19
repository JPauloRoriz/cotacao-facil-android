package com.example.cotacaofacil.presentation.viewmodel.register

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.domain.exception.*
import com.example.cotacaofacil.domain.usecase.register.contract.RegisterUseCase
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.register.model.RegisterEvent
import com.example.cotacaofacil.presentation.viewmodel.register.model.RegisterState
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val context: Context
) : ViewModel() {
    val stateLiveData = MutableLiveData(RegisterState())
    val eventLiveData = SingleLiveEvent<RegisterEvent>()


    suspend fun tapOnRegister(
        cnpj: String,
        login: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            stateLiveData.setLoadingRegister(true)
            registerUseCase.invoke(
                cnpj = cnpj,
                login = login,
                password = password,
                confirmPassword = confirmPassword,
                userTypeSelected = stateLiveData.value?.userTypeSelected ?: UserTypeSelected()
            ).onSuccess {
                stateLiveData.setLoadingRegister(false)
                eventLiveData.value =
                    RegisterEvent.SuccessRegister(context.getString(R.string.register_success))
            }.onFailure { error ->
                when (error) {
                    is EmptyFildException -> {
                        stateLiveData.setMessageErrorRegister(context.getString(R.string.empty_fild))
                    }
                    is PasswordLenghtException -> {
                        stateLiveData.setMessageErrorRegister(context.getString(R.string.password_lenght))
                    }
                    is EmailInvalidException -> {
                        stateLiveData.setMessageErrorRegister(context.getString(R.string.email_or_password_invald))
                    }
                    is PasswordNotConfirmException -> {
                        stateLiveData.setMessageErrorRegister(context.getString(R.string.password_not_confirm))
                    }
                    is NoConnectionInternetException -> {
                        stateLiveData.setMessageErrorRegister(context.getString(R.string.not_internet))
                    }
                    is EmailExistingException -> {
                        stateLiveData.setMessageErrorRegister(context.getString(R.string.email_existing))
                    }
                    is CnpjInvalidException -> {
                        stateLiveData.setMessageErrorRegister(error.message.toString())
                    }
                    is UserTypeEmptyException -> {
                        stateLiveData.setMessageErrorRegister(context.getString(R.string.user_type_emptu))
                    }
                    is CnpjExistingException -> {
                        stateLiveData.setMessageErrorRegister(context.getString(R.string.cnpj_existing))
                    }
                    is ErrorSaveBodyCompany -> {
                        stateLiveData.setMessageErrorRegister(context.getString(R.string.not_possible_add_user))
                    }
                    is DefaultException -> {
                        stateLiveData.setMessageErrorRegister(context.getString(R.string.not_possible_add_user))
                    }
                    else -> {
                        stateLiveData.setMessageErrorRegister(error.message.toString())
                    }
                }
            }
        }
    }

    fun enterCnpj() {
        eventLiveData.value = RegisterEvent.EnterCnpj
    }

    fun MutableLiveData<RegisterState>.setLoadingRegister(value: Boolean) {
        this.value = this.value?.copy(isLoading = value)
    }

    fun MutableLiveData<RegisterState>.setUserProviderType(value: Boolean) {
        this.value = this.value?.copy(
            userTypeSelected = UserTypeSelected(
                userBuyerSelected = !value,
                userProviderSelected = value
            )
        )
    }

    fun MutableLiveData<RegisterState>.setUserBuyerType(value: Boolean) {
        this.value = this.value?.copy(
            userTypeSelected =
            UserTypeSelected(
                userProviderSelected = !value,
                userBuyerSelected = value
            )
        )
    }


    fun MutableLiveData<RegisterState>.setMessageErrorRegister(message: String) {
        this.value = this.value?.copy(isLoading = false, messageError = message)
    }

    fun tapOnUserBuyerSelected() {
        stateLiveData.setUserBuyerType(true)
    }

    fun tapOnUserProviderSelected() {
        stateLiveData.setUserProviderType(true)
    }


}
