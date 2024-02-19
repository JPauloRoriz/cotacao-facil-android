package com.example.cotacaofacil.data.repository.user.contract

import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected

interface UserRepository {
    suspend fun addUser(cnpj : String, login : String, password : String, userTypeSelected: UserTypeSelected, nameUser : String): Result<Any?>
    suspend fun getUser(email : String, password : String): Result<UserModel>
}