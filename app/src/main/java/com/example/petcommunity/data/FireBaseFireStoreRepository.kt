package com.example.petcommunity.data

import com.example.petcommunity.model.OtpPhoneNumber
import com.example.petcommunity.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBaseFireStoreRepository @Inject constructor(private val firebaseFireStore: FirebaseFirestore) {

    companion object {
        const val OTP = "OTP"
        const val Users = "Users"

    }

    suspend fun saveOtpSMS(phoneNumber: String, otp: String) {
        val data = hashMapOf(
            "otp" to otp,
        )
        firebaseFireStore.collection(OTP).document(phoneNumber).set(data).await()
    }


    suspend fun saveUser(
        email: String,
        password: String,
        userName: String,
        phoneNumber: String,
        dateOfBirth: String,
        gender: String,
        address: String
    ) {
        firebaseFireStore.collection(Users)
            .add(User(email, password, userName, phoneNumber, dateOfBirth, gender, address)).await()
    }
}