package com.example.cotacaofacil.data.service.user.contract

import com.example.cotacaofacil.data.model.UserResponse
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected

interface UserService {
    suspend fun saveUser(cnpj: String, email: String, password: String, userTypeSelected : UserTypeSelected, nameUser : String): Result<Any?>
    suspend fun getUserFirebase(email: String, password: String): Result<UserResponse>

}