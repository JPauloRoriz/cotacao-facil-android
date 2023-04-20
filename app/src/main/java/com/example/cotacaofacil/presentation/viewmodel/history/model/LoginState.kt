package com.example.cotacaofacil.presentation.viewmodel.history.model

import com.example.cotacaofacil.domain.model.HistoryModel

data class HistoryState(
    var isLoading: Boolean = false,
    var messageError: String = "",
    var historicModelList : MutableList<HistoryModel> = mutableListOf(),
    var showImageError : Boolean = false,
    val showCheckBox: Boolean = false
) {

}