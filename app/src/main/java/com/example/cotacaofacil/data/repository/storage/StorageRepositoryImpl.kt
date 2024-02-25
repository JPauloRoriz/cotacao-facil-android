package com.example.cotacaofacil.data.repository.storage

import android.graphics.Bitmap
import com.example.cotacaofacil.data.repository.storage.contract.StorageRepository
import com.example.cotacaofacil.data.service.storage.contract.StorageService

class StorageRepositoryImpl(
    private val service : StorageService
) : StorageRepository {
    override suspend fun editImageUser(cnpj : String, imageBitmap : Bitmap): Result<String> {
        return service.editImageUser(cnpjUser = cnpj, imageBitmap = imageBitmap)
    }

    override suspend fun getImageProfile(cnpj: String): Result<String> {
        return service.getImageUser(cnpj)
    }

    override suspend fun getAllImagesByCnpj(cnpjs : MutableList<String>): Result<MutableList<String>> {
       return service.getAllImagesByCnpj(cnpjs)
    }
}