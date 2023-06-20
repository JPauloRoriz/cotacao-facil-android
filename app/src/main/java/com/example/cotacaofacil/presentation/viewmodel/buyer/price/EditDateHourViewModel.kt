package com.example.cotacaofacil.presentation.viewmodel.buyer.price


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.domain.usecase.date.contract.DateCurrentUseCase
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractDateHourPrice.DateEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractDateHourPrice.DateState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class EditDateHourViewModel(
    private val dateCurrentUseCase: DateCurrentUseCase,
    private val context: Context,

    ) : ViewModel() {

    val event = SingleLiveEvent<DateEvent>()
    val state = MutableLiveData(DateState())

    fun setupView(dateFinishPrice: Long?, hour: Int) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateFinishPrice ?: System.currentTimeMillis()

        if (hour < 12) {
            state.postValue(state.value?.copy(isNight = false, messageError = ""))
        } else {
            state.postValue(state.value?.copy(isNight = true, messageError = ""))
        }
        state.postValue(state.value?.copy(messageError = "", date = calendar))
        event.postValue(DateEvent.UpdateDate(calendar))

    }

    fun changeHour(hourOfDay: Int) {
        if (hourOfDay < 12) {
            state.postValue(state.value?.copy(isNight = false, messageError = ""))
        } else {
            state.postValue(state.value?.copy(isNight = true, messageError = ""))
        }
    }

    fun tapOnSaveDate(year: Int?, month: Int?, dayOfMonth: Int?, hour: Int?, minute: Int?, hourDistance: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dateCurrentUseCase.invoke()
                .onSuccess { currentDate ->
                    val calendar: Calendar = Calendar.getInstance()
                    if (year != null && month != null && dayOfMonth != null) {
                        calendar.set(year, month, dayOfMonth)
                    }
                    hour?.let { calendar.set(Calendar.HOUR_OF_DAY, it) }
                    minute?.let { calendar.set(Calendar.MINUTE, it) }
                    calendar.set(Calendar.SECOND, 0)
                    val dateSelect = calendar.timeInMillis
                    val currentDateOneHourLater = Calendar.getInstance()
                    currentDateOneHourLater.timeInMillis = currentDate
                    currentDateOneHourLater.add(Calendar.HOUR_OF_DAY, hourDistance)
                    if (dateSelect < currentDate) {
                        state.postValue(state.value?.copy(messageError = context.getString(R.string.not_possible_adjust_date)))
                    } else if (dateSelect < currentDateOneHourLater.timeInMillis) {
                        state.postValue(
                            state.value?.copy(
                                messageError = context.getString(
                                    R.string.not_possible_adjust_date_on_hour_later,
                                    hourDistance.toString()
                                )
                            )
                        )
                    } else {
                        event.postValue(DateEvent.SaveDate(dateSelect))
                    }
                }
                .onFailure {

                }
        }
    }
}