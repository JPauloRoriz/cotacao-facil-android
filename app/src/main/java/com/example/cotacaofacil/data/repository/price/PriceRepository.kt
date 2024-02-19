package com.example.cotacaofacil.data.repository.price

import com.example.cotacaofacil.domain.model.PriceEditModel
import com.example.cotacaofacil.domain.model.PriceModel
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected

interface PriceRepository {
    suspend fun savePrice(priceModel: PriceModel) : Result<String>
    suspend fun editPrice(priceModel: PriceModel) : Result<String>
    suspend fun getPricesByCnpj(cnpjUser: String, userTypeSelected: UserTypeSelected, userModel : UserModel): Result<MutableList<PriceModel>>

    suspend fun getPricesProvider(cnpjBuyers: MutableList<String>, cnpjProvider : String, userModel: UserModel) : Result<MutableList<PriceModel>>
    suspend fun setPricesPartner(cnpjPartner: String, productsEditPrice: PriceEditModel): Result<Any>
    suspend fun getPriceByCode(priceCode : String, cnpjBuyerCreator : String) : Result <PriceModel>
}