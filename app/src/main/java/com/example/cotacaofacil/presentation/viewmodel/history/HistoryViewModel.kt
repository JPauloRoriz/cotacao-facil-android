package com.example.cotacaofacil.presentation.viewmodel.history

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.domain.exception.HistoricEmptyException
import com.example.cotacaofacil.domain.model.HistoryModel
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.domain.usecase.history.contract.GetAllItemHistoryUseCase
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.history.model.HistoryEvent
import com.example.cotacaofacil.presentation.viewmodel.history.model.HistoryState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val context: Context,
    private val getAllItemHistoryUseCase: GetAllItemHistoryUseCase,
    private val user: UserModel
) : ViewModel() {
    val stateLiveData = MutableLiveData(HistoryState())
    val eventLiveData = SingleLiveEvent<HistoryEvent>()

    init {
        getAllItemHistory()
    }

    private fun getAllItemHistory() {
        stateLiveData.postValue(HistoryState(isLoading = true, "", showImageError = false))
        viewModelScope.launch(Dispatchers.IO) {
            getAllItemHistoryUseCase.invoke(user.cnpj)
                .onSuccess {
                    val historyModelList = mutableListOf<HistoryModel>()
                    historyModelList.addAll(it as MutableList<HistoryModel>)
                    stateLiveData.postValue(
                        HistoryState().copy(
                            isLoading = false,
                            messageError = "",
                            historicModelList = historyModelList.sortedByDescending { historyModel -> historyModel.date }.toMutableList(),
                            showImageError = false
                        )
                    )
                }
                .onFailure {
                    when (it) {
                        is HistoricEmptyException -> {
                            stateLiveData.postValue(
                                HistoryState().copy(
                                    isLoading = false,
                                    messageError = context.getString(R.string.message_error_historic_empty),
                                    showImageError = true
                                )
                            )
                        }
                    }
                }
        }
    }

}