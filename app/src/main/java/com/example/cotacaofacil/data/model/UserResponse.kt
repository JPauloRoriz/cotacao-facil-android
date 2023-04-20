package com.example.cotacaofacil.data.model

import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected

data class UserResponse(
    var id: String = "",
    val cnpj: String = "",
    val email: String = "",
    val userTypeSelected : UserTypeSelected = UserTypeSelected(
        userProviderSelected = true,
        userBuyerSelected = false
    )
)