package com.data.local

import android.content.Context
import javax.inject.Inject

class LocalDataRepository  @Inject constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE)

    fun setRunFirst() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isRunFirst", false)
        editor.apply()
    }

    fun getRunFirst(): Boolean {
        return sharedPreferences.getBoolean("isRunFirst", true)
    }

    fun getStayLogged(): Boolean {
        return sharedPreferences.getBoolean("isStayLogged", false)
    }

    fun getEmail(): String {
        return sharedPreferences.getString("email", "").toString()
    }

    fun getPassword(): String {
        return sharedPreferences.getString("password", "").toString()
    }

    fun setEmail(email:String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()
    }

    fun setPassword(password:String) {
        val editor = sharedPreferences.edit()
        editor.putString("password", password)
        editor.apply()
    }

    fun removeAccount(){
        val editor = sharedPreferences.edit()
        editor.remove("email")
        editor.remove("password")
        editor.apply()

    }

    fun setStayLogged(isStayLogged: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isStayLogged", isStayLogged)
        editor.apply()

    }
}