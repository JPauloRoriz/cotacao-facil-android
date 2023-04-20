package com.example.cotacaofacil.domain.usecase.register.contract

import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected

interface RegisterUseCase {
    suspend fun invoke(
        cnpj: String,
        login: String,
        password: String,
        confirmPassword: String,
        userTypeSelected: UserTypeSelected
    ) : Result<Any?>
}