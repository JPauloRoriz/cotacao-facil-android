package com.example.cotacaofacil.data.service.storage

import android.graphics.Bitmap
import com.example.cotacaofacil.data.service.storage.contract.StorageService
import com.example.cotacaofacil.domain.exception.DefaultException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class StorageServiceImpl(
    private val storage: FirebaseStorage
) : StorageService {
    override suspend fun editImageUser(cnpjUser: String, imageBitmap: Bitmap): Result<String> {
        return try {
            val storageRef: StorageReference = storage.reference
            val path = "$PREFIX_IMAGE/$cnpjUser.jpg"
            val fileRef: StorageReference = storageRef.child(path)
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val dataImage = baos.toByteArray()
            fileRef.putBytes(dataImage)
                .await()
            val url = fileRef.downloadUrl.await().toString()
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getImageUser(cnpj: String): Result<String> {
        return try {
            val storageRef: StorageReference = storage.reference
            val path = "$PREFIX_IMAGE/$cnpj.jpg"
            val fileRef: StorageReference = storageRef.child(path)

            val url = fileRef.downloadUrl.await().toString()

            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllImagesByCnpj(cnpjs: MutableList<String>): Result<MutableList<String>> {
       return try {
            val imagesList = mutableListOf<String>()
            cnpjs.forEach { cnpj ->
                getImageUser(cnpj).onSuccess { image ->
                    imagesList.add(image)
                }
            }
           Result.success(imagesList)
        } catch (e : java.lang.Exception) {
            Result.failure(DefaultException())
        }
    }


    companion object {
        private const val PREFIX_IMAGE = "images_user"
    }

}