package com.example.cotacaofacil.presentation.viewmodel.buyer.price

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.domain.exception.*
import com.example.cotacaofacil.domain.model.*
import com.example.cotacaofacil.domain.usecase.date.contract.CalculationDateFinishPriceUseCase
import com.example.cotacaofacil.domain.usecase.date.contract.DateCurrentUseCase
import com.example.cotacaofacil.domain.usecase.date.contract.ValidationNextCreatePriceUseCase
import com.example.cotacaofacil.domain.usecase.partner.contract.GetAllPartnerModelUseCase
import com.example.cotacaofacil.presentation.ui.extension.toFormattedDateTime
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractCreatePrice.CreatePriceEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractCreatePrice.CreatePriceState
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CreatePriceViewModel(
    private val getAllPartnerModelUseCase: GetAllPartnerModelUseCase,
    private val userHelper: UserHelper,
    private val currentDateUseCase: DateCurrentUseCase,
    private val calculationDateFinishPriceUseCase: CalculationDateFinishPriceUseCase,
    private val validationNextCreatePriceUseCase: ValidationNextCreatePriceUseCase,
    private val context: Context
) : ViewModel() {
    val createPriceEventLiveData = SingleLiveEvent<CreatePriceEvent>()
    val createPriceStateLiveData = MutableLiveData(CreatePriceState())

    val user: UserModel? = userHelper.user
    var currentDate: String = System.currentTimeMillis().toFormattedDateTime()
    private var showEffect: Boolean = true
    var listAllPartners: MutableList<PartnerModel> = mutableListOf()
    var date: String = ""

    init {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        date = currentDate
        createPriceStateLiveData.postValue(
            createPriceStateLiveData.value?.copy(
                showProgressBar = true,
                dateFinishPrice = currentDate,
                dateDelivery = calendar.timeInMillis.toFormattedDateTime()
            )
        )
        viewModelScope.launch(Dispatchers.IO) {
            currentDateUseCase.invoke()
                .onSuccess { date ->
                    currentDate = calculationDateFinishPriceUseCase.invoke(date)
                    user?.id?.let { userId ->
                        user.cnpj.let { userCnpj ->
                            getAllPartnerModelUseCase.invoke(userTypeSelected = UserTypeSelected(userBuyerSelected = true), userId, userCnpj)
                                .onSuccess {
//                                    val result = PartnerMock.listPartner()
                                    val result = it.filter { it.isMyPartner == StatusIsMyPartner.TRUE }.toMutableList()
                                    if (result.isNotEmpty()) {
                                        listAllPartners = result
                                        createPriceStateLiveData.postValue(
                                            createPriceStateLiveData.value?.copy(
                                                listPartnersSelect = result,
                                                showProgressBar = false,
                                                messageErrorPartners = ""
                                            )
                                        )
                                    } else {
                                        createPriceStateLiveData.postValue(
                                            createPriceStateLiveData.value?.copy(
                                                showProgressBar = false,
                                                messageErrorPartners = context.getString(R.string.not_find_partner),
                                                listPartnersSelect = mutableListOf()
                                            )
                                        )
                                    }
                                }
                                .onFailure {
                                    createPriceStateLiveData.postValue(createPriceStateLiveData.value?.copy(showProgressBar = false))
                                }
                        }
                    }
                }
                .onFailure {

                }
        }
    }

    fun tapOnSwitchAutoClose(checked: Boolean) {
        if (checked)
            createPriceEventLiveData.postValue(CreatePriceEvent.AutoChecked)
        else
            createPriceEventLiveData.postValue(CreatePriceEvent.NotAutoChecked)
    }

    fun tapOnSwitchAllPartners(checked: Boolean, listPartner: MutableList<PartnerModel>) {
        if (showEffect) {
            if (checked) {
                createPriceEventLiveData.postValue(CreatePriceEvent.AllPartnersWithEffect)
            } else {
                createPriceEventLiveData.postValue(CreatePriceEvent.NotAllPartnersWithEffect)
            }
        } else {
            if (checked) {
                createPriceEventLiveData.postValue(CreatePriceEvent.AllPartners)
            }

        }
        showEffect = true
    }

    fun filterSearch(text: CharSequence?) {
        val listMyPartners = listAllPartners.filter { it.isMyPartner == StatusIsMyPartner.TRUE }.toMutableList()
        if (text?.isEmpty() == true) {
            createPriceStateLiveData.postValue(createPriceStateLiveData.value?.copy(listPartnersSelect = listMyPartners, messageErrorPartners = ""))
        } else {
            val resultFilter: MutableList<PartnerModel> = mutableListOf()
            listMyPartners.forEach {
                if (text?.let { it1 -> it.cnpjCorporation.contains(it1, true) } == true) {
                    resultFilter.add(it)
                }
            }
            if (resultFilter.isNotEmpty()) {
                createPriceStateLiveData.postValue(createPriceStateLiveData.value?.copy(listPartnersSelect = resultFilter, messageErrorPartners = ""))
            } else {
                createPriceStateLiveData.postValue(
                    createPriceStateLiveData.value?.copy(
                        messageErrorPartners = context.getString(R.string.not_find_partner),
                        listPartnersSelect = mutableListOf()
                    )
                )
            }
        }

    }

//    fun filterSearch(text: CharSequence?) {
//        viewModelScope.launch(Dispatchers.IO) {
//            user?.userTypeSelected?.let { user.id?.let { it1 -> getAllPartnerModelUseCase.invoke(it, it1, user.cnpj) } }
//                ?.onSuccess { listResult ->
//                    val listPartner = listResult.filter { it.isMyPartner == StatusIsMyPartner.TRUE }.toMutableList()
//                    val resultFilter: MutableList<PartnerModel> = mutableListOf()
//                    if (text?.isEmpty() == true) {
//                        createPriceEvent.postValue(CreatePriceEvent.UpdateListEvent(listPartner))
//                    } else {
//                        listPartner.forEach {
//                            if (text?.let { it1 -> it.cnpjCorporation.contains(it1, true) } == true) {
//                                resultFilter.add(it)
//                            }
//                        }
//                        if (resultFilter.isNotEmpty()) {
//                            createPriceState.postValue(createPriceState.value?.copy(listPartnersSelect = resultFilter, messageErrorPartners = ""))
//                        } else {
//                            createPriceState.postValue(
//                                createPriceState.value?.copy(
//                                    messageErrorPartners = context.getString(R.string.not_find_partner),
//                                    listPartnersSelect = mutableListOf()
//                                )
//                            )
//                        }
//                    }
//                }
//                ?.onFailure {
//                    createPriceState.postValue(
//                        createPriceState.value?.copy(
//                            listPartnersSelect = mutableListOf(),
//                            messageErrorPartners = context.getString(R.string.message_error_find_partners)
//                        )
//                    )
//                }
//
//        }
//
//    }

    fun tapOnButtonNext(
        autoClose: Boolean,
        allowAllPartners: Boolean,
        date: Long,
        dateDelivery: Long,
        partners: MutableList<PartnerModel>,
        priority: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            currentDateUseCase.invoke()
                .onSuccess { currentDate ->
                    val dateFinish: Long? = if (autoClose) null else date
                    validationNextCreatePriceUseCase.invoke(
                        autoClose,
                        allowAllPartners,
                        dateFinish,
                        dateDelivery,
                        partners,
                        priority,
                        currentDate
                    )
                        .onSuccess {
                            val priorityPrice: PriorityPrice = when (priority) {
                                0 -> {
                                    PriorityPrice.HIGH
                                }
                                1 -> {
                                    PriorityPrice.AVERAGE
                                }
                                2 -> {
                                    PriorityPrice.LOW
                                }
                                else -> {
                                    PriorityPrice.AVERAGE
                                }
                            }
                            val priceModel = user?.cnpj?.let { cnpj ->
                                PriceModel(
                                    code = "",
                                    productSPrice = mutableListOf(),
                                    partnersAuthorized = partners,
                                    dateStartPrice = currentDate,
                                    dateFinishPrice = dateFinish,
                                    priority = priorityPrice,
                                    cnpjBuyerCreator = cnpj
                                )
                            } ?: PriceModel()
                            createPriceEventLiveData.postValue(CreatePriceEvent.SuccessNext(priceModel))

                        }
                        .onFailure { exception ->
                            when (exception) {
                                is ScheduleDateForPastException -> {
                                    createPriceStateLiveData.postValue(createPriceStateLiveData.value?.copy(messageError = context.getString(R.string.not_possible_adjust_date)))
                                }
                                is StLeastOneHourException -> {
                                    createPriceStateLiveData.postValue(
                                        createPriceStateLiveData.value?.copy(
                                            messageError = context.getString(
                                                R.string.not_possible_adjust_date_on_hour_later,
                                                "1"
                                            )
                                        )
                                    )
                                }
                                is MinTwoProvidersInPriceException -> {
                                    createPriceStateLiveData.postValue(createPriceStateLiveData.value?.copy(messageError = context.getString(R.string.min_two_partners)))
                                }
                                is PriorityIsEmptyException -> {
                                    createPriceStateLiveData.postValue(createPriceStateLiveData.value?.copy(messageError = context.getString(R.string.priority_is_empty)))
                                }
                                is StLeastOneDayException -> {
                                    createPriceStateLiveData.postValue(
                                        createPriceStateLiveData.value?.copy(
                                            messageError = context.getString(
                                                R.string.not_possible_adjust_date_on_hour_later,
                                                "24"
                                            )
                                        )
                                    )
                                }
                            }
                        }
                }
                .onFailure {

                }

        }
    }

    fun tapOnSelectAll(checked: Boolean) {
        showEffect = false
        createPriceEventLiveData.postValue(CreatePriceEvent.AllPartners)
        createPriceEventLiveData.postValue(CreatePriceEvent.ChangeSwitch(true))
    }

    fun checkAllPartners(listPartner: MutableList<PartnerModel>, notShowEffect: Boolean) {
        val partnersChecked = listPartner.filter { it.isChecked }
        if (partnersChecked.size == listPartner.size) {
            createPriceEventLiveData.postValue(CreatePriceEvent.ChangeSwitch(true))
        } else {
            createPriceEventLiveData.postValue(CreatePriceEvent.ChangeSwitch(false))
        }
        showEffect = false

    }

    fun updateHourFinishPrice(hour: Long, isFinishPrice : Boolean) {
        if(isFinishPrice){
            createPriceStateLiveData.postValue(createPriceStateLiveData.value?.copy(dateFinishPrice = hour.toFormattedDateTime()))
        } else {
            createPriceStateLiveData.postValue(createPriceStateLiveData.value?.copy(dateDelivery = hour.toFormattedDateTime()))
        }
    }
}