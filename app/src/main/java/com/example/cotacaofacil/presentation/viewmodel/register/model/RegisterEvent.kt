package com.example.cotacaofacil.presentation.viewmodel.register.model

sealed class RegisterEvent {

    data class SuccessRegister(
        val message: String
    ) : RegisterEvent()

    object EnterCnpj : RegisterEvent()

}