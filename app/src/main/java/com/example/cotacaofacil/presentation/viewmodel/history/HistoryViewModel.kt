package com.example.cotacaofacil.presentation.viewmodel.history

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.domain.exception.HistoricEmptyException
import com.example.cotacaofacil.domain.model.HistoryModel
import com.example.cotacaofacil.domain.usecase.history.contract.DeleteHistoricUseCase
import com.example.cotacaofacil.domain.usecase.history.contract.GetAllItemHistoryUseCase
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.history.model.HistoryEvent
import com.example.cotacaofacil.presentation.viewmodel.history.model.HistoryState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val context: Context,
    private val getAllItemHistoryUseCase: GetAllItemHistoryUseCase,
    private val deleteHistoricUseCase: DeleteHistoricUseCase,
    private val userHelper: UserHelper
) : ViewModel() {
    val stateLiveData = MutableLiveData(HistoryState())
    val eventLiveData = SingleLiveEvent<HistoryEvent>()

    private val user = userHelper.user

    init {
        getAllItemHistory()
    }

    fun getAllItemHistory() {
        stateLiveData.postValue(stateLiveData.value?.copy(isLoading = true, "", showImageError = false))
        viewModelScope.launch(Dispatchers.IO) {
            user?.cnpj?.let {
                getAllItemHistoryUseCase.invoke(it)
                    .onSuccess {
                        val historyModelList = mutableListOf<HistoryModel>()
                        historyModelList.addAll(it as MutableList<HistoryModel>)
                        stateLiveData.postValue(
                            stateLiveData.value?.copy(
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
                                    stateLiveData.value?.copy(
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

    fun tapOnBack() {
        eventLiveData.postValue(HistoryEvent.TapOnBack)
    }

    fun tapOnDeleteHistoric(historyModel: HistoryModel) {
        viewModelScope.launch (Dispatchers.IO){
            val historicList = stateLiveData.value?.historicModelList
            deleteHistoricUseCase.invoke(historyModel)
                ?.onSuccess {
                    historicList?.remove(historyModel)
                    stateLiveData.postValue(historicList?.let { it1 -> stateLiveData.value?.copy(historicModelList = it1) })
                    eventLiveData.postValue(historicList?.let { it1 -> HistoryEvent.MessageSuccess(context.getString(R.string.historic_deleted), it1) })
                }?.onFailure {
                    eventLiveData.postValue(historicList?.let { it1 -> HistoryEvent.MessageSuccess(context.getString(R.string.error_delete_historic), it1) })
                }

        }
    }

}