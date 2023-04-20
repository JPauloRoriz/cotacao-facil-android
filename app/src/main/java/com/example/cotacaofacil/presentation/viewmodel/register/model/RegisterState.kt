package com.example.cotacaofacil.presentation.viewmodel.register.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class RegisterState(
    val isLoading: Boolean = false,
    val messageError: String = "",
    val userTypeSelected: UserTypeSelected = UserTypeSelected(
        userProviderSelected = false,
        userBuyerSelected = false
    )
)

@Parcelize
data class UserTypeSelected(
    val userProviderSelected: Boolean = false,
    val userBuyerSelected: Boolean = false
) : Parcelable