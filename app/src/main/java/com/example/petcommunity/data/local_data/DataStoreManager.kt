package com.example.petcommunity.data.local_data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DataStoreManager @Inject constructor( context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app")
    private val dataStore = context.dataStore

    companion object {
        val isRunFirst = booleanPreferencesKey("isRunFirst")
        val isStayLoggedIn = booleanPreferencesKey("isStayLoggedIn")

    }

    suspend fun setRunFirst() {

        dataStore.edit {
            it[isRunFirst] = false
        }
    }


    fun getRunFirst(): Flow<Boolean> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            val isRunFirst = pref[isRunFirst] ?: false
            isRunFirst
        }
    }

    suspend fun setIsStayLoggedIn(isStay:Boolean) {
        dataStore.edit {
            it[isStayLoggedIn] = isStay
        }
    }

  suspend  fun getIsStayLoggedIn(): Flow<Boolean> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            val isStayLogged = pref[isStayLoggedIn] ?: false
            isStayLogged
        }
    }
}