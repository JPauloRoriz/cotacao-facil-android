package com.example.cotacaofacil.data.model.util

import com.example.cotacaofacil.data.model.BodyCompanyResponse
import com.example.cotacaofacil.data.model.HistoricResponse
import com.example.cotacaofacil.data.model.PriceResponse
import com.example.cotacaofacil.data.model.ProductPriceResponse
import com.example.cotacaofacil.domain.Extensions.Companion.toProductModel
import com.example.cotacaofacil.domain.Extensions.Companion.toProductResponse
import com.example.cotacaofacil.domain.model.*

fun BodyCompanyResponse.toBodyCompanyModel(): BodyCompanyModel {
    return BodyCompanyModel(
        nome, uf, situacao, municipio, fantasia, abertura, status, telefone, email
    )
}

fun BodyCompanyResponse.toPartnerModel(idUser: String, cnpj: String, date: Long): PartnerModel {
    return PartnerModel(
        id = "",
        idUser = idUser,
        nameCorporation = this.nome,
        nameFantasy = fantasia,
        cnpjCorporation = cnpj,
        isMyPartner = StatusIsMyPartner.TRUE,
        date = date
    )
}

fun Result<MutableList<HistoricResponse>>.toHistoricModel(): Result<Any> {
    val historicModelList = mutableListOf<HistoryModel>()

    return this.fold(
        onSuccess = {
            it.forEach { historicResponse ->
                historicModelList.add(
                    HistoryModel(
                        historicResponse.date,
                        historicResponse.type,
                        historicResponse.nameAssistant
                    )
                )
            }
            Result.success(historicModelList)
        },
        onFailure = {
            Result.failure<Throwable>(it)
        }

    )
}


fun PriceResponse.toPriceModel() : PriceModel{
    return PriceModel(
        code = code,
        productsPrice = productsPrice.toListProductsPriceModel(),
        partnersAuthorized = partnersAuthorized,
        nameCompanyCreator = nameCompanyCreator,
        dateStartPrice = dateStartPrice,
        dateFinishPrice = dateFinishPrice,
        priority = priority,
        cnpjBuyerCreator = cnpjBuyerCreator,
        closeAutomatic = closeAutomatic,
        allowAllProvider = allowAllProvider,
        deliveryDate = deliveryDate,
        description = description,
        status = status
    )
}
fun PriceModel.toPriceResponse(): PriceResponse {
    return PriceResponse(
        code = code,
        productsPrice = productsPrice.toListProductsPriceResponse(),
        partnersAuthorized = partnersAuthorized,
        nameCompanyCreator = nameCompanyCreator,
        dateStartPrice = dateStartPrice,
        dateFinishPrice = dateFinishPrice,
        priority = priority,
        cnpjBuyerCreator = cnpjBuyerCreator,
        closeAutomatic = closeAutomatic,
        allowAllProvider = allowAllProvider,
        deliveryDate = deliveryDate,
        description = description,
        status = status
    )
}

fun MutableList<ProductPriceModel>.toListProductsPriceResponse(): MutableList<ProductPriceResponse> {
    return map {
       it.toProductPriceResponse()
    }.toMutableList()
}

fun MutableList<ProductPriceResponse>.toListProductsPriceModel(): MutableList<ProductPriceModel> {
    return map {
        it.toProductPriceModel()
    }.toMutableList()
}

fun ProductPriceModel.toProductPriceResponse(): ProductPriceResponse {
    return ProductPriceResponse(
        productModel = productModel.toProductResponse(),
        usersPrice = usersPrice,
        quantityProducts = quantityProducts
    )
}

fun ProductPriceResponse.toProductPriceModel(): ProductPriceModel {
    return ProductPriceModel(
        productModel = productModel.toProductModel(),
        usersPrice = usersPrice,
        quantityProducts = quantityProducts
    )
}

fun  MutableList<ProductPriceModel>.toMutableListProductPriceEditModel(){

}


