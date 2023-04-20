package com.example.cotacaofacil.domain.usecase.partner.contract

import android.content.Context
import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.UserModel

interface AddRequestPartnerUseCase {
    suspend fun invoke(userModel: UserModel, partner: PartnerModel, context: Context): Result<Unit?>
}