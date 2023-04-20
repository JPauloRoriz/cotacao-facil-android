package com.example.cotacaofacil.data.model.util

import com.example.cotacaofacil.data.model.BodyCompanyResponse
import com.example.cotacaofacil.data.model.HistoricResponse
import com.example.cotacaofacil.domain.model.BodyCompanyModel
import com.example.cotacaofacil.domain.model.HistoryModel
import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.StatusIsMyPartner

fun BodyCompanyResponse.toBodyCompanyModel(): BodyCompanyModel {
    return BodyCompanyModel(
        nome, uf, situacao, municipio, fantasia, abertura, status
    )
}

fun BodyCompanyResponse.toPartnerModel(idUser: String, cnpj: String): PartnerModel {
    return PartnerModel(
        id = "",
        idUser = idUser,
        nameCorporation = this.nome,
        nameFantasy = fantasia,
        cnpjCorporation = cnpj,
        isMyPartner = StatusIsMyPartner.TRUE
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



