package com.example.cotacaofacil.data.repository.user

import com.example.cotacaofacil.data.repository.user.contract.UserRepository
import com.example.cotacaofacil.data.service.user.contract.UserService
import com.example.cotacaofacil.domain.exception.*
import com.example.cotacaofacil.domain.mapper.mapper
import com.example.cotacaofacil.domain.mapper.mapperUser
import com.example.cotacaofacil.domain.model.BodyCompanyModel
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import java.io.IOException

class UserRepositoryImpl(
    private val userService: UserService
) : UserRepository {
    override suspend fun addUser(cnpj: String, login: String, password: String, userTypeSelected: UserTypeSelected, nameUser : String): Result<Any?> {
            return userService.saveUser(cnpj, login, password, userTypeSelected, nameUser).recoverCatching { error ->
                return when (error) {
                    is IOException -> Result.failure(NoConnectionInternetException())
                    is FirebaseAuthInvalidCredentialsException -> Result.failure(
                        EmailInvalidException()
                    )
                    is FirebaseAuthUserCollisionException -> Result.failure(EmailExistingException())
                    is FirebaseNetworkException -> Result.failure(NoConnectionInternetException())
                    is CnpjExistingException -> Result.failure(CnpjExistingException())
                    else -> Result.failure(DefaultException())
                }
        }
    }

    override suspend fun getUser(email: String, password: String): Result<UserModel> {
        return userService.getUserFirebase(email, password).mapperUser().recoverCatching { error ->
            return when (error) {
                is IOException -> Result.failure(NoConnectionInternetException())
                is FirebaseAuthInvalidCredentialsException -> Result.failure(
                    EmailOrPasswordInvalidException()
                )
                is FirebaseAuthInvalidUserException -> Result.failure(
                    EmailOrPasswordInvalidException()
                )

                is FirebaseNetworkException -> Result.failure(
                    NoConnectionInternetException()
                )
                else -> Result.failure(DefaultException())
            }
        }
    }



}