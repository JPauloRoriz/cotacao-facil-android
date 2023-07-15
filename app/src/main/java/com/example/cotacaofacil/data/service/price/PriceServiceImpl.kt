package com.example.cotacaofacil.data.service.price

import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.data.service.price.contract.PriceService
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.exception.ListEmptyException
import com.example.cotacaofacil.domain.exception.NoConnectionInternetException
import com.example.cotacaofacil.domain.exception.NotCreatePriceException
import com.example.cotacaofacil.domain.model.PriceModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.IOException
import kotlin.random.Random

class PriceServiceImpl(
    private val firestore: FirebaseFirestore,
    private val userHelper: UserHelper
) : PriceService {
    override suspend fun savePrice(priceModel: PriceModel): Result<String> {
        val result = userHelper.user?.cnpj?.let { cnpj ->

            priceModel.code = createPriceCode(cnpj)
            firestore.collection(PRICE_TABLE).document(cnpj).collection(MY_PRICES).document(priceModel.code)
                .set(priceModel)
        }
        result?.await()
        return try {
            if (result?.isSuccessful == true) {
                Result.success(priceModel.code)
            } else {
                Result.failure(NotCreatePriceException())
            }
        } catch (e: java.lang.Exception) {
            Result.failure(NotCreatePriceException())
        }
    }

    override suspend fun getPricesByCnpj(cnpjUser: String): Result<MutableList<PriceModel>> {
        val result = firestore.collection(PRICE_TABLE).document(cnpjUser)
            .collection(MY_PRICES).get().await()
        return try {
            if (result.documents.isEmpty())
                Result.failure(ListEmptyException())
            else {
                val productsList = result.toObjects(PriceModel::class.java).filterNotNull().sortedByDescending { it.dateStartPrice }
                Result.success(productsList.toMutableList())
            }
        } catch (e: Exception) {
            when (e) {
                is IOException -> Result.failure(NoConnectionInternetException())
                is FirebaseNetworkException -> Result.failure(NoConnectionInternetException())
                else -> Result.failure(DefaultException())
            }
        }
    }

    private suspend fun createPriceCode(cnpjBuyer: String): String {
        var productCode = Random.nextInt(100000, 999999)
        val productsRef = firestore.collection(PRICE_TABLE).document(cnpjBuyer).collection(MY_PRICES)
        var codeExists = true

        while (codeExists) {
            val query = productsRef.whereEqualTo("code", productCode)
            val result = query.get().await()

            if (result.isEmpty) {
                codeExists = false
            } else {
                productCode = Random.nextInt(100000, 999999)
            }
        }

        return productCode.toString()
    }

    companion object {
        private const val PRICE_TABLE = "price"
        private const val MY_PRICES = "my_prices"
    }
}