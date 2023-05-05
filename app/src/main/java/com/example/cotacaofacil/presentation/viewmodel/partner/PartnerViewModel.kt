package com.example.cotacaofacil.presentation.viewmodel.partner

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.domain.exception.*
import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.StatusIsMyPartner
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.domain.usecase.partner.contract.*
import com.example.cotacaofacil.domain.usecase.partner.util.TypeDeletePartner
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.partner.model.PartnerEvent
import com.example.cotacaofacil.presentation.viewmodel.partner.model.PartnerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PartnerViewModel(
    private val context: Context,
    private val validationCnpjUseCase: ValidationCnpjUseCase,
    private val getAllPartnerModelUseCase: GetAllPartnerModelUseCase,
    private val addRequestPartnerUseCase: AddRequestPartnerUseCase,
    private val rejectRequestPartnerUseCase: RejectRequestPartnerUseCase,
    private val acceptRequestPartnerUseCase: AcceptRequestPartnerUseCase,
    private val userHelper : UserHelper
) : ViewModel() {
    val stateLiveData = MutableLiveData(PartnerState())
    val eventLiveData = SingleLiveEvent<PartnerEvent>()
    val user = userHelper.user
    init {
        loadListPartnerModel(true)
    }

    fun loadListPartnerModel(isAll: Boolean) {
        stateLiveData.postValue(PartnerState().copy(isLoading = true))
        viewModelScope.launch(Dispatchers.IO) {
            user?.id?.let {
                getAllPartnerModelUseCase.invoke(user.userTypeSelected, it, user.cnpj)
                    .onSuccess { listPartnerModel ->
                        val listFilter = listPartnersFilter(isAll, listPartnerModel)
                        if (listFilter.isEmpty()) {
                            stateLiveData.postValue(
                               stateLiveData.value?.copy(
                                    showImageError = false,
                                    isLoading = false,
                                    messageError = setMessageError(isAll),
                                    numberNotifications = listPartnersFilter(false, listPartnerModel).size.toString()
                                )
                            )

                        } else {
                            stateLiveData.postValue(
                                stateLiveData.value?.copy(
                                    textTitleList = setTitleList(isAll),
                                    isLoading = false,
                                    showImageError = false,
                                    listPartnerModel = listFilter,
                                    numberNotifications = listPartnersFilter(false, listPartnerModel).size.toString()
                                )
                            )
                        }
                    }.onFailure {
                        it.message

                    }
            }
        }
    }

    private fun listPartnersFilter(
        isAllPartners: Boolean,
        listAllPartners: MutableList<PartnerModel>
    ): MutableList<PartnerModel> {
        return if (isAllPartners) {
            listAllPartners.filter { partnerModel ->
                partnerModel.isMyPartner != StatusIsMyPartner.TO_RESPOND
            }.toMutableList()
        } else {
            listAllPartners.filter { partnerModel ->
                partnerModel.isMyPartner == StatusIsMyPartner.TO_RESPOND
            }.toMutableList()
        }
    }


    suspend fun tapOnAddNewPartner(cnpj: String) {
        stateLiveData.value = PartnerState().copy(textTitleList = setTitleList(true), isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            user.let { user ->
                user?.userTypeSelected?.let {7
                    validationCnpjUseCase.invoke(it, user, cnpj, context)
                        .onSuccess { partnerModel ->
                            val listPartner = mutableListOf<PartnerModel>()
                            partnerModel?.let { listPartner.add(partnerModel) }
                            eventLiveData.postValue(PartnerEvent.GoToAddNewPartnerSuccess)
                            stateLiveData.postValue(
                                PartnerState().copy(
                                    textTitleList = setTitleList(true),
                                    listPartnerModel = listPartner,
                                    isLoading = false
                                )
                            )
                        }.onFailure { error ->
                            eventLiveData.postValue(PartnerEvent.GoToAddNewPartnerError)
                            when (error) {
                                is EmptyFildException -> {
                                    eventLiveData.postValue(PartnerEvent.FindEmpty)
                                }
                                is CnpjOwnException -> {
                                    stateLiveData.postValue(
                                        PartnerState().copy(
                                            textTitleList = setTitleList(true),
                                            listPartnerModel = mutableListOf(),
                                            isLoading = false,
                                            messageError = context.getString(R.string.owner_erro_message),
                                            showImageError = true
                                        )
                                    )

                                }
                                is CnpjIncompleteException -> {
                                    stateLiveData.postValue(
                                        PartnerState().copy(
                                            textTitleList = setTitleList(true),
                                            listPartnerModel = mutableListOf(),
                                            isLoading = false,
                                            messageError = context.getString(R.string.cnpj_invalid_exception),
                                            showImageError = true
                                        )
                                    )
                                }
                                is NoConnectionInternetException -> {
                                    eventLiveData.postValue(PartnerEvent.ErrorInternetConnection(context.getString(R.string.not_internet)))
                                }
                                is UserNotFindException -> {
                                    stateLiveData.postValue(
                                        PartnerState().copy(
                                            textTitleList = setTitleList(true),
                                            listPartnerModel = mutableListOf(),
                                            isLoading = false,
                                            messageError = context.getString(R.string.not_cnpj_find),
                                            showImageError = true
                                        )
                                    )
                                }
                                is DefaultException -> {
                                    stateLiveData.postValue(
                                        PartnerState().copy(
                                            textTitleList = setTitleList(true),
                                            listPartnerModel = mutableListOf(),
                                            isLoading = false,
                                            messageError = context.getString(R.string.default_exception_find),
                                            showImageError = true
                                        )
                                    )
                                }
                            }
                        }
                }
            }
        }
    }

    fun enterCnpj() {
        eventLiveData.value = PartnerEvent.EnterCnpj
    }

    suspend fun tapOnIconPartner(partner: PartnerModel) {
        viewModelScope.launch(Dispatchers.IO) {
            when (partner.isMyPartner) {
                StatusIsMyPartner.FALSE -> {
                    user?.let {
                        addRequestPartnerUseCase.invoke(it, partner, context)
                            .onSuccess {
                                loadListPartnerModel(true)
                                eventLiveData.postValue(PartnerEvent.RequestAddPartner)
                            }
                            .onFailure {
                                when (it) {
                                    is NoConnectionInternetException -> {
                                        stateLiveData.postValue(
                                            PartnerState().copy(
                                                textTitleList = setTitleList(true),
                                                listPartnerModel = mutableListOf(),
                                                messageError = context.getString(
                                                    R.string.not_internet
                                                )
                                            )
                                        )
                                    }
                                    else -> {
                                        stateLiveData.postValue(
                                            PartnerState().copy(
                                                textTitleList = setTitleList(true),
                                                messageError = context.getString(
                                                    R.string.not_possibility_add_partner
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                    }
                }
                StatusIsMyPartner.TRUE -> {
                    eventLiveData.postValue(PartnerEvent.DeletePartner(partner))
                }
                StatusIsMyPartner.WAIT_ANSWER -> {
                    eventLiveData.postValue(PartnerEvent.CancelRequestPartner(partner))
                }
                StatusIsMyPartner.TO_RESPOND -> {
                    //Onde muda o constraint para aceitar ou recusar a solicitação
                }
            }
        }
    }

    fun tapOnRejectPartner(partner: PartnerModel) {
        eventLiveData.postValue(PartnerEvent.RejectPartner(partner))
    }

    suspend fun deletePartner(){

    }

    suspend fun tapOnConfirmRejectPartner(partner: PartnerModel, typeDeletePartner: TypeDeletePartner) {
        stateLiveData.postValue(PartnerState(isLoading = true))
        user?.cnpj?.let {
            rejectRequestPartnerUseCase.invoke(it, partner, context, typeDeletePartner)
                .onSuccess {
                    eventLiveData.postValue(PartnerEvent.SuccessRejectPartner)
                    loadListPartnerModel(true)
                }.onFailure {
                    when (it) {
                        is NoConnectionInternetException -> {
                            stateLiveData.postValue(
                                PartnerState().copy(
                                    textTitleList = setTitleList(true),
                                    listPartnerModel = mutableListOf(),
                                    messageError = context.getString(
                                        R.string.not_internet
                                    )
                                )
                            )
                        }
                        else -> {
                            stateLiveData.postValue(
                                PartnerState(
                                    isLoading = true, messageError = context.getString(
                                        R.string.inpossible_reject_request_partner
                                    )
                                )
                            )
                        }
                    }
                }
        }
    }

    suspend fun tapOnConfirmDeletePartner(partner: PartnerModel) {
        stateLiveData.postValue(PartnerState(isLoading = true))
        user?.cnpj?.let {
            rejectRequestPartnerUseCase.invoke(it, partner, context, TypeDeletePartner.DELETE_PARTNER)
                .onSuccess {
                    eventLiveData.postValue(PartnerEvent.SuccessDeletePartner)
                    loadListPartnerModel(true)
                }.onFailure {
                    stateLiveData.postValue(
                        PartnerState(
                            isLoading = true, messageError = context.getString(
                                R.string.inpossible_delete_partner, partner.nameFantasy
                            )
                        )
                    )
                }
        }
    }

    suspend fun tapOnAcceptPartner(partner: PartnerModel) {
        user?.cnpj?.let {
            acceptRequestPartnerUseCase.invoke(it, partner)
                .onSuccess {
                    eventLiveData.postValue(PartnerEvent.SuccessAcceptPartner)
                    loadListPartnerModel(false)
                }.onFailure {
                    stateLiveData.postValue(
                        PartnerState(
                            isLoading = true, messageError = context.getString(
                                R.string.inpossible_accept_request_partner
                            )
                        )
                    )
                }
        }
    }

    private fun listRequests(listAllPartners: MutableList<PartnerModel>): MutableList<PartnerModel> {
        return listAllPartners.filter { partnerModel ->
            partnerModel.isMyPartner == StatusIsMyPartner.TO_RESPOND
        }.toMutableList()
    }

    private fun setTitleList(isAll: Boolean): String {
        return if (isAll) {
            context.getString(R.string.providers)
        } else {
            context.getString(R.string.request)
        }
    }

    private fun setMessageError(isAll: Boolean): String {
        return if (isAll) {
            context.getString(
                R.string.partner_empty_message_error
            )
        } else {
            context.getString(
                R.string.request_empty_message_error
            )
        }
    }
}