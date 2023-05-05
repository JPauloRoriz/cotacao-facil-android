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
                ProductModel(
                    it.name,
                    it.description,
                    it.brand,
                    it.typeMeasurement,
                    it.cnpjBuyer,
                    it.code,
                    it.quantity

                )
            }.toMutableList()
        }

    }
}