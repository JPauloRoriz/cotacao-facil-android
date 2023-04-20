package com.example.cotacaofacil.domain.usecase.partner.contract

import android.content.Context
import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected

interface ValidationCnpjUseCase {
    suspend fun invoke(userTypeSelected: UserTypeSelected, user: UserModel, cnpj: String, context: Context): Result<PartnerModel?>
}