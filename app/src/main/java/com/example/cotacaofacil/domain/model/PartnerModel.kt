package com.example.cotacaofacil.domain.model

data class PartnerModel(
    val id: String,
    val idUser: String,
    val nameFantasy: String?,
    val nameCorporation: String?,
    val cnpjCorporation: String,
    var isMyPartner: StatusIsMyPartner = StatusIsMyPartner.FALSE
)

enum class StatusIsMyPartner {
    TRUE,
    FALSE,
    WAIT_ANSWER,
    TO_RESPOND
}

