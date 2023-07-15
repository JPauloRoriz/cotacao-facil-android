package com.example.cotacaofacil.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PartnerModel(
    val id: String = "",
    val idUser: String = "",
    val nameFantasy: String? = "",
    val nameCorporation: String? = "",
    val cnpjCorporation: String = "",
    var isChecked: Boolean = false,
    var date: Long = 0,
    var isMyPartner: StatusIsMyPartner = StatusIsMyPartner.FALSE
) : Parcelable

enum class StatusIsMyPartner {
    TRUE,
    FALSE,
    WAIT_ANSWER,
    TO_RESPOND
}

