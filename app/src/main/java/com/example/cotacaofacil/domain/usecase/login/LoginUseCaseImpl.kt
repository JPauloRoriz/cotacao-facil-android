package com.example.cotacaofacil.domain.usecase.login

import android.content.Context
import com.example.cotacaofacil.R
import com.example.cotacaofacil.data.repository.user.contract.UserRepository
import com.example.cotacaofacil.domain.exception.EmptyFildException
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.domain.usecase.login.contract.LoginUseCase

class LoginUseCaseImpl(
    private val context : Context,
    private val repository: UserRepository
) : LoginUseCase {
    override suspend fun invoke(email: String, password: String): Result<UserModel> {
        return if (email.isEmpty() || password.isEmpty()) {
            Result.failure(EmptyFildException())
        } else {
            return repository.getUser(email, password)
        }
    }
}