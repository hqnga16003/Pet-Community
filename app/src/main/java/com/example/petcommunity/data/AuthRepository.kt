package com.example.petcommunity.data

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

import javax.inject.Inject

class AuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {

     fun login(email: String, password: String): Flow<Resource<AuthResult>> {

         return flow {
             emit(Resource.Loading())
             val result = firebaseAuth.signInWithEmailAndPassword(email,password).await()
             emit(Resource.Success(result))
         }.catch {
             emit(Resource.Error(message = it.message.toString()))

         }
    }

}