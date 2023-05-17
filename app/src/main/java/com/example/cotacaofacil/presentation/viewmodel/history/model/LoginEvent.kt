package com.example.cotacaofacil.presentation.viewmodel.history.model

import com.example.cotacaofacil.domain.model.HistoryModel

sealed class HistoryEvent {
    object TapOnBack: HistoryEvent()
    data class MessageSuccess(var message : String, var newListHistoric : MutableList<HistoryModel>): HistoryEvent()

}