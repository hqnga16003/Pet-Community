package com.example.petcommunity.model

data class User(
    val email: String,
    val password: String,
    val username: String,
    val phoneNumber: String,
    val dateOfBirth: String,
    val gender: String,
    val address: String,
) {
}