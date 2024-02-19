package com.example.cotacaofacil.data.service.user

import com.example.cotacaofacil.data.model.BodyCompanyResponse
import com.example.cotacaofacil.data.model.PartnerResponse
import com.example.cotacaofacil.data.model.UserResponse
import com.example.cotacaofacil.data.service.user.contract.UserService
import com.example.cotacaofacil.domain.exception.CnpjExistingException
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.exception.UserNotFindException
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserFirebaseService(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserService {
    private val usersFirebase by lazy {
        firestore.collection(USERS_TABLE)
    }

    override suspend fun saveUser(cnpj: String, email: String, password: String, userTypeSelected : UserTypeSelected, nameUser : String): Result<Any?> {
        return try {
            val resultAuth = auth.createUserWithEmailAndPassword(email, password).await()
            val cnpjExisting =
                firestore.collection(USERS_TABLE).whereEqualTo("cnpj", cnpj).limit(1).get()
                    .await().documentChanges.size > 0
            if (cnpjExisting) {
                resultAuth.user?.delete()
                Result.failure(CnpjExistingException())
            } else {
                val newUser = UserResponse(resultAuth.user?.uid ?: "", cnpj, email, userTypeSelected, nameUser)

                val user = usersFirebase.document(newUser.id).set(newUser)
                if (user.isSuccessful) {
                    Result.success(null)
                } else {
                    user.exception.runCatching {
                    }
                }
            }

        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun getUserFirebase(email: String, password: String): Result<UserResponse> {

        return try {
            val resultAuth = auth.signInWithEmailAndPassword(email, password).await()

            val uid: String = resultAuth.user?.uid.toString()
            val resultUser = usersFirebase.document(uid).get().await()
            resultUser.toObject(UserResponse::class.java)?.let { userResponse ->
                Result.success(userResponse)
            } ?: run {
                Result.failure(DefaultException())
            }
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }



    companion object {
        private const val USERS_TABLE = "users"
    }
}