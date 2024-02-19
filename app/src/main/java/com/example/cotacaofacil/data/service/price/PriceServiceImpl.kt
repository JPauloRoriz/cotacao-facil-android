package com.example.cotacaofacil.data.service.price

import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.data.model.PriceResponse
import com.example.cotacaofacil.data.service.price.contract.PriceService
import com.example.cotacaofacil.domain.exception.*
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.IOException
import kotlin.random.Random

class PriceServiceImpl(
    private val firestore: FirebaseFirestore,
    private val userHelper: UserHelper
) : PriceService {
    override suspend fun savePrice(priceResponse: PriceResponse): Result<String> {
        val user = userHelper.user
        val cnpj = user?.cnpj
        if (cnpj != null) {
            priceResponse.code = createPriceCode(cnpj)
            try {
                val documentReference = firestore.collection(PRICE_TABLE)
                    .document(cnpj)
                    .collection(MY_PRICES)
                    .document(priceResponse.code)

                documentReference.addSnapshotListener { documentSnapshot, _ ->
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Result.success(priceResponse.code)
                    }
                }

                documentReference.set(priceResponse).await()
                return Result.success(priceResponse.code)
            } catch (e: Exception) {
                return Result.failure(NotCreatePriceException())
            }
        }
        return Result.failure(DefaultException())
    }

    override suspend fun editPrice(priceResponse: PriceResponse): Result<String> {
        return try {
            val result =
                firestore.collection(PRICE_TABLE).document(priceResponse.cnpjBuyerCreator).collection(MY_PRICES).document(priceResponse.code)
                    .set(priceResponse)
        result.await()
            if (result.isSuccessful) {
                Result.success(priceResponse.code)
            } else {
                Result.failure(NotCreatePriceException())
            }
        } catch (e: java.lang.Exception) {
            Result.failure(NotCreatePriceException())
        }
    }

    override suspend fun getPricesByCnpj(cnpjUser: String): Result<MutableList<PriceResponse>> {
        return try {
            val result = firestore.collection(PRICE_TABLE).document(cnpjUser)
                .collection(MY_PRICES).get().await()
            if (result.documents.isEmpty())
                Result.failure(ListEmptyException())
            else {
                val productsList = result.toObjects(PriceResponse::class.java)
                productsList.filterNotNull().sortedByDescending { it.dateStartPrice }
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

    override suspend fun getPriceByCnpj(code: String, cnpjBuyer: String): Result<PriceResponse> {
        return try {
            val result = firestore.collection(PRICE_TABLE).document(cnpjBuyer)
                .collection(MY_PRICES).document(code).get().await()
            if (!result.exists())
                Result.failure(PriceNotFindException())
            else {
                val product = result.toObject(PriceResponse::class.java)
                product?.let { Result.success(it) } ?: Result.failure(PriceNotFindException())
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