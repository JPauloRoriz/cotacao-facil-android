package com.example.cotacaofacil.data.service.history

import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.data.model.HistoricResponse
import com.example.cotacaofacil.data.service.history.contract.HistoryService
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.exception.HistoricEmptyException
import com.example.cotacaofacil.domain.exception.NoConnectionInternetException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.IOException


class HistoryServiceImpl(
    private val firestore: FirebaseFirestore,
    private val userHelper: UserHelper
) : HistoryService {
    override suspend fun getAllHistoryResponseByCnpj(cnpj: String): Result<MutableList<HistoricResponse>> {
        val historyResponseList =
            firestore.collection(HISTORY_TABLE).document(cnpj).collection(MY_HISTORIC).get().await()
        return if (historyResponseList.isEmpty) {
            Result.failure(HistoricEmptyException())
        } else {
            try {
                historyResponseList.toObjects(HistoricResponse::class.java).let { listHistoric ->
                    Result.success(listHistoric)
                }
            } catch (e: Exception) {
                when (e) {
                    is IOException -> Result.failure(NoConnectionInternetException())
                    is FirebaseNetworkException -> Result.failure(NoConnectionInternetException())
                    else -> Result.failure(e)
                }
            }
        }
    }

    override suspend fun addHistoryResponse(toHistoricResponse: HistoricResponse, cnpj: String): Result<Any?>? {
        val result = firestore.collection(HISTORY_TABLE).document(cnpj).collection(MY_HISTORIC).document().set(toHistoricResponse)
        result.await()
        return if (result.isSuccessful) {
            Result.success(null)
        } else {
            result.exception?.let { Result.failure(it) }
        }
    }

    override suspend fun deleteHistoric(historicResponse: HistoricResponse): Result<Any?>? {
        val userCnpj: String? = userHelper.user?.cnpj
        if (userCnpj != null) {
           val result = firestore.collection(HISTORY_TABLE)
                .document(userCnpj)
                .collection(MY_HISTORIC)
                .whereEqualTo("date", historicResponse.date)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        document.reference.delete()
                    }
                    // Operação de exclusão concluída com sucesso
                }
                .addOnFailureListener {
                    // Ocorreu um erro ao tentar excluir o documento
                }
            result.await()

            return if (result.isSuccessful) {
                Result.success(null)
            } else {
                result.exception?.let { Result.failure(it) }
            }
        } else {
            return Result.failure(DefaultException())
        }
    }

    companion object {
        const val HISTORY_TABLE = "historic"
        const val MY_HISTORIC = "my_historic"
    }
}