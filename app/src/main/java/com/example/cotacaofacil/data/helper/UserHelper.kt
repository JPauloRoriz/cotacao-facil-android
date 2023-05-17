package com.example.cotacaofacil.data.helper

import com.example.cotacaofacil.domain.model.UserModel

class UserHelper {
    var user : UserModel? = null
    fun isLogged() = user != null
}