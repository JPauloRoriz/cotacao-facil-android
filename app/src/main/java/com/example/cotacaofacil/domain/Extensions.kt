package com.example.cotacaofacil.domain

import android.content.Context
import android.net.ConnectivityManager
import com.example.cotacaofacil.data.model.ProductResponse
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.model.ProductModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await


class Extensions {
    companion object {
        fun String.convertCnpj(): String {
            return this.replace(Regex("[/.-]"), "")
        }

        fun String.formatCnpj(): String {
            return "${this.substring(0, 2)}.${this.substring(2, 5)}.${this.substring(5, 8)}/${this.substring(8, 12)}-${this.substring(12)}"
        }

        suspend fun Task<QuerySnapshot>.toResult(): Result<Unit?> {
            return try {
                if (this.isSuccessful) {
                    this.await()
                    Result.success(null)
                } else {
                    Result.failure(DefaultException())
                }
            } catch (e: Exception) {
                Result.failure(DefaultException())
            }
        }

        fun String?.ifNotEmpty(): String {
            return if (this.isNullOrEmpty()) {
                "Nome não registrado"
            } else {
                this
            }
        }

        fun Context.isNetworkConnected(): Boolean {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        }

        fun MutableList<ProductResponse>.toProductModel() :MutableList<ProductModel>{
          return this.map {
               it.toProductModel()
            }.toMutableList()
        }

        fun ProductResponse.toProductModel() : ProductModel{
           return ProductModel(
               name,
               description,
               brand,
               typeMeasurement,
               cnpjBuyer,
               code,
               quantity,
               favorite,
               date
               )
        }

        fun ProductModel.toProductResponse() : ProductResponse{
            return ProductResponse(
                name,
                description,
                brand,
                typeMeasurement,
                cnpjBuyer,
                code,
                quantity,
                date,
                isFavorite

            )
        }

    }
}