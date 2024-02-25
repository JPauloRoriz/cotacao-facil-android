package com.example.cotacaofacil.data.service.storage.contract

import android.graphics.Bitmap

interface StorageService {
    suspend fun editImageUser(cnpjUser: String, imageBitmap: Bitmap) : Result<String>
    suspend fun getImageUser(cnpj: String): Result<String>
    suspend fun getAllImagesByCnpj(cnpjs: MutableList<String>): Result<MutableList<String>>
}