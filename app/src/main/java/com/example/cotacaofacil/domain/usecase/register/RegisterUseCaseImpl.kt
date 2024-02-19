package com.example.cotacaofacil.domain.usecase.register

import android.content.Context
import com.example.cotacaofacil.R
import com.example.cotacaofacil.data.repository.bodyCompany.contract.BodyCompanyRepository
import com.example.cotacaofacil.data.repository.user.contract.UserRepository
import com.example.cotacaofacil.domain.Extensions.Companion.convertCnpj
import com.example.cotacaofacil.domain.exception.*
import com.example.cotacaofacil.domain.usecase.register.contract.RegisterUseCase
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected
import java.net.UnknownHostException

class RegisterUseCaseImpl(
    private val userRepository: UserRepository,
    private val context: Context,
    private val bodyCompanyRepository: BodyCompanyRepository
) : RegisterUseCase {

    override suspend fun invoke(
        cnpj: String,
        login: String,
        password: String,
        confirmPassword: String,
        userTypeSelected: UserTypeSelected
    ): Result<Any?> {

        return if (cnpj.isEmpty() || login.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Result.failure(EmptyFildException())
        } else if (password.length < 6) {
            Result.failure(PasswordLenghtException())
        } else if (password != confirmPassword) {
            Result.failure(PasswordNotConfirmException())
        } else if (!userTypeSelected.userBuyerSelected && !userTypeSelected.userProviderSelected) {
            Result.failure(UserTypeEmptyException())
        } else {
            val result = bodyCompanyRepository.getBodyCompanyModel(cnpj.convertCnpj())
            if (result.isSuccess) {
                val bodyCompanyModel = result.getOrNull()

                if (bodyCompanyModel?.status == context.getString(R.string.error)) {
                    return Result.failure(CnpjInvalidException(context.getString(R.string.cnpj_invalid_exception)))
                } else {
                    try {
                        bodyCompanyRepository.addBodyCompanyModel(userTypeSelected, cnpj.convertCnpj(), bodyCompanyModel)
                        return userRepository.addUser(
                            cnpj = cnpj.convertCnpj(),
                            login = login,
                            password = password,
                            userTypeSelected = userTypeSelected,
                            nameUser = bodyCompanyModel?.nome ?: ""
                        )
                    } catch (e: Exception) {
                        if (e == ErrorSaveBodyCompany()) {
                            return Result.failure(ErrorSaveBodyCompany())
                        } else {
                            return Result.failure(DefaultException())
                        }
                    }
                }

            } else {
                return when (result.exceptionOrNull()) {
                    is UnknownHostException -> {
                        Result.failure(NoConnectionInternetException())
                    }
                    is CnpjExistingExceptionResponse -> {
                        Result.failure(CnpjExistingException())
                    }
                    else -> {
                        return Result.failure(result.exceptionOrNull() ?: Exception())
                    }
                }
            }


        }

    }

}

