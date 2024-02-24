package com.example.petcommunity.data

sealed class Resource(){
    data object Success : Resource()
    data class Error(val message: String) : Resource()
    data object Loading : Resource()

}