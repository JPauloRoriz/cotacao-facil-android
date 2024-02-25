package com.example.cotacaofacil.presentation.viewmodel.partner.model

import com.example.cotacaofacil.databinding.ItemPartnerBinding
import com.example.cotacaofacil.domain.model.PartnerModel

sealed class PartnerEvent {
    object GoToAddNewPartnerSuccess : PartnerEvent()
    object GoToAddNewPartnerError : PartnerEvent()
    object EnterCnpj : PartnerEvent()
    object RequestAddPartner : PartnerEvent()
    object SuccessRejectPartner : PartnerEvent()
    object SuccessDeletePartner : PartnerEvent()
    object SuccessAcceptPartner : PartnerEvent()
    object FindEmpty : PartnerEvent()
    object TapOnBack : PartnerEvent()
    data class RejectPartner(val partner : PartnerModel) : PartnerEvent()
    data class DeletePartner(val partner : PartnerModel) : PartnerEvent()
    data class CancelRequestPartner(val partner : PartnerModel) : PartnerEvent()
    data class ErrorInternetConnection(val message : String) : PartnerEvent()


}