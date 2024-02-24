package com.example.petcommunity.data.local_data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject


class DataStoreManager @Inject constructor(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app")
    private val dataStore = context.dataStore
    private val sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE)

    companion object {
    }

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