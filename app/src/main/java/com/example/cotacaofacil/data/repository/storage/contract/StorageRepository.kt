package com.example.cotacaofacil.data.repository.storage.contract

import android.graphics.Bitmap

interface StorageRepository {
    suspend fun editImageUser(cnpj: String, imageBitmap: Bitmap): Result<String>
    suspend fun getImageProfile(cnpj: String) : Result<String>
    suspend fun getAllImagesByCnpj(cnpjs :MutableList<String>): Result<MutableList<String>>
}