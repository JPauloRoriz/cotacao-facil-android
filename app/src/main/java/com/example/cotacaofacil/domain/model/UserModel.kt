package com.example.cotacaofacil.domain.model

import android.os.Parcelable
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var id: String? = null,
    val cnpj : String = "",
    val email : String = "",
    val userTypeSelected : UserTypeSelected = UserTypeSelected(
        userProviderSelected = false,
        userBuyerSelected = false
    ),
    var nameCorporation : String = ""
) : Parcelable