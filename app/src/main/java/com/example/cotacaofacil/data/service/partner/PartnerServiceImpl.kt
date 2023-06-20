package com.example.cotacaofacil.data.service.partner

import com.example.cotacaofacil.data.model.BodyCompanyResponse
import com.example.cotacaofacil.data.model.PartnerResponse
import com.example.cotacaofacil.data.service.cnpj.BodyCompanyServiceImpl.Companion.BODY_COMPANY
import com.example.cotacaofacil.data.service.partner.contract.PartnerService
import com.example.cotacaofacil.domain.exception.*
import com.example.cotacaofacil.domain.model.StatusIsMyPartner
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.IOException

class PartnerServiceImpl(
    private val firestore: FirebaseFirestore
) : PartnerService {
    override suspend fun getBodyCompanyFirebaseByCnpj(
        userTypeSelected: UserTypeSelected,
        idUser: String,
        cnpj: String
    ): Result<BodyCompanyResponse?> {
        val bodyCompany =
            firestore.collection(BODY_COMPANY).document(cnpj).get().await()
        return if (!bodyCompany.exists()) {
            Result.failure(UserNotFindException())
        } else {
            try {
                bodyCompany.toObject(BodyCompanyResponse::class.java).let { bodyComponyResponse ->
                        if(userTypeSelected == bodyComponyResponse?.typeUser){
                            Result.failure(UserNotFindException())
                        }else{
                            bodyComponyResponse?.cnpj = cnpj
                            Result.success(bodyComponyResponse)
                        }
                }
            } catch (e: Exception) {
                when(e) {
                    is IOException -> Result.failure(NoConnectionInternetException())
                    is FirebaseNetworkException -> Result.failure(NoConnectionInternetException())
                    else -> Result.failure(e)
                }
            }
        }
    }

    override suspend fun getPartnersByCnpj(cnpjUser: String): Result<MutableList<PartnerResponse>> {
        return runCatching {
           firestore.collection(PARTNER).document(cnpjUser).collection(MY_PARTNERS).get()
                .await().toObjects(PartnerResponse::class.java)
        }
    }

    override suspend fun addRequestPartnerResponse(
        cnpjUser: String,
        partnerResponse: PartnerResponse
    ): Result<Unit?> {
        return try {
            val result =
                firestore.collection(PARTNER).document(cnpjUser).collection(MY_PARTNERS)
                    .document(partnerResponse.cnpjUser)
                    .set(partnerResponse)

            val secoundResult =
                firestore.collection(PARTNER).document(partnerResponse.cnpjUser)
                    .collection(MY_PARTNERS)
                    .document(cnpjUser)
                    .set(PartnerResponse(cnpjUser, false, cnpjRequestingUser = cnpjUser, date = partnerResponse.date))

            result.await()
            secoundResult.await()
            if (result.isSuccessful && secoundResult.isSuccessful) {
                Result.success(null)
            } else {
                Result.failure(PartnerRequestNotSendException())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isMyPartner(cnpjUser: String, cnpjFind: String): StatusIsMyPartner {
        val partner = getPartnersByCnpj(cnpjUser).getOrNull()?: emptyList()
        val partnerSelected = partner.filter { it.cnpjUser == cnpjFind }
        return if (partner.isNotEmpty() && (partnerSelected.isNotEmpty() || cnpjFind.isEmpty())) {
            val partnerFind = partnerSelected.ifEmpty { partner }
            if (partnerFind[0].approved) {
                StatusIsMyPartner.TRUE
            } else {
                if (partnerFind[0].cnpjRequestingUser == cnpjUser) {
                    StatusIsMyPartner.WAIT_ANSWER
                } else {
                    StatusIsMyPartner.TO_RESPOND
                }
            }
        } else {
            StatusIsMyPartner.FALSE
        }
    }

    override suspend fun rejectPartner(
        cnpj: String,
        partnerResponse: PartnerResponse
    ): Result<Boolean> {
        val deleteFirstRequest =
            firestore.collection(PARTNER).document(cnpj).collection(MY_PARTNERS)
                .document(partnerResponse.cnpjUser)
                .delete()

        val deleteSecoundRequest =
            firestore.collection(PARTNER).document(partnerResponse.cnpjUser).collection(MY_PARTNERS)
                .document(cnpj)
                .delete()
        deleteFirstRequest.await()
        deleteSecoundRequest.await()
        return if (deleteFirstRequest.isSuccessful && deleteSecoundRequest.isSuccessful) {
            Result.success(true)
        } else {
            Result.failure(NotRejectException())
        }
    }

    override suspend fun acceptPartner(
        cnpj: String,
        partnerResponse: PartnerResponse
    ): Result<Boolean> {
        partnerResponse.approved = true
        return try {
            val result =
                firestore.collection(PARTNER).document(cnpj).collection(MY_PARTNERS)
                    .document(partnerResponse.cnpjUser)
                    .set(partnerResponse)

            val secoundResult =
                firestore.collection(PARTNER).document(partnerResponse.cnpjUser)
                    .collection(MY_PARTNERS)
                    .document(cnpj)
                    .set(PartnerResponse(cnpj, true, cnpjRequestingUser = partnerResponse.cnpjUser, date = partnerResponse.date))
            result.await()
            secoundResult.await()
            if (result.isSuccessful && secoundResult.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(RequestPartnerNotAccept())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private const val PARTNER = "partner"
        private const val MY_PARTNERS = "my_partners"
    }
}