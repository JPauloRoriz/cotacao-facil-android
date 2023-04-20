package com.example.cotacaofacil.presentation.viewmodel.login.model

import com.example.cotacaofacil.domain.model.UserModel

sealed class LoginEvent {
    object GoToRegister: LoginEvent()
    data class SuccessLoginProvider(val user : UserModel): LoginEvent()
    data class SuccessLoginBuyer(val user : UserModel): LoginEvent()
}