package com.example.cotacaofacil.domain.usecase.login.contract

import com.example.cotacaofacil.domain.model.UserModel

interface LoginUseCase {
    suspend fun invoke(email: String, password: String): Result<UserModel>
}