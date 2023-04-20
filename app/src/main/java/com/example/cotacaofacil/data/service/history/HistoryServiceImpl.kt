package com.example.cotacaofacil.data.service.history

import com.example.cotacaofacil.data.model.HistoricResponse
import com.example.cotacaofacil.data.service.history.contract.HistoryService
import com.example.cotacaofacil.domain.exception.HistoricEmptyException
import com.example.cotacaofacil.domain.exception.NoConnectionInternetException
import com.example.cotacaofacil.domain.model.TypeHistory
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.IOException

class HistoryServiceImpl(
    private val firestore: FirebaseFirestore
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

    override suspend fun addHistoryResponse(toHistoricResponse: HistoricResponse, cnpj: String) : Result<Any?>? {
     val result = firestore.collection(HISTORY_TABLE).document(cnpj).collection(MY_HISTORIC).document().set(toHistoricResponse)
        result.await()
        return if (result.isSuccessful){
            Result.success(null)
        } else {
            result.exception?.let { Result.failure(it) }
        }
    }

    companion object {
        const val HISTORY_TABLE = "historic"
        const val MY_HISTORIC = "my_historic"
    }
}