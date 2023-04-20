package com.example.cotacaofacil.presentation.viewmodel.history.model

import com.example.cotacaofacil.domain.model.UserModel

sealed class HistoryEvent {
    object GoToRegister: HistoryEvent()
    data class SuccessLoginProvider(val user : UserModel): HistoryEvent()
    data class SuccessLoginBuyer(val user : UserModel): HistoryEvent()
}