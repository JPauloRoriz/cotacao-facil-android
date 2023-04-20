package com.example.cotacaofacil.domain.model

data class HistoryModel(
    val date: Long = 0L,
    val typeHistory: TypeHistory,
    val nameAssistant: String = "",
    var isSelected: Boolean = false
)

enum class TypeHistory {
    CREATE_PRICE,
    CANCEL_PRICE,
    FINISH_PRICE,
    SEND_REQUEST_PARTNER,
    SEND_RECEIVE_PARTNER,
    NEW_PARTNER_ADD,
    REQUEST_PARTNER_REJECT,
    REQUEST_PARTNER_CANCEL,
    PARTNER_DELETED,
    MY_REQUEST_PARTNER_REJECT
}
