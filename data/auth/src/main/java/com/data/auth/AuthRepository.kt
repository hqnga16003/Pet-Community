package com.data.auth

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID

import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFireStore: FirebaseFirestore
) {
    fun login(email: String, password: String): Flow<Resource> {
        return flow {
            emit(Resource.Loading)
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success)
        }.catch {
            emit(Resource.Error(message = it.message.toString()))
        }
    }

    fun signInWithCredential(
        phoneNumber: String,
        otpSMS: String
    ): Flow<Resource> {
        return flow {
            emit(Resource.Loading)
            val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                getVerificationId(phoneNumber), otpSMS
            )
            firebaseAuth.signInWithCredential(credential).await()
            emit(Resource.Success)
        }.catch {
            emit(Resource.Error(message = it.message.toString()))
        }
    }



    suspend fun register(
        email: String,
        password: String,
        username: String,
        avatar: Uri?
    ){
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        if (avatar != null) {
            firebaseStorage.reference.child("avatar/${result.user?.uid}").putFile(avatar)
                .await()
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .setPhotoUri(firebaseStorage.reference.child("avatar/${result.user?.uid}").downloadUrl.await())
                    .build()
            )?.await()
        } else {
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(username).build()
            )?.await()
        }
    }
    private suspend fun getVerificationId(phoneNumber: String): String {
        val result =
            firebaseFireStore.collection("OTP").document(phoneNumber)
                .get().await()

        return result.get("otp").toString()

    }

    suspend fun deleteAccountPhoneNumber(){
        firebaseAuth.currentUser?.delete()?.await()
    }
}