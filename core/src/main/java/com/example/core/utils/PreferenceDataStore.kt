package com.example.core.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore : DataStore<Preferences> by preferencesDataStore("auth_status")

interface AuthDataStore {
    fun isAuth() : Flow<Boolean>

    suspend fun setIsAuth(value: Boolean)
}


class AuthDataStoreImp @Inject constructor(
    @ApplicationContext val context: Context
): AuthDataStore {

    companion object {
        val PREFERENCE_KEY = booleanPreferencesKey("auth_status")
    }

    override fun isAuth(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[PREFERENCE_KEY] ?: false
        }
    }

    override suspend fun setIsAuth(value: Boolean) {
        context.dataStore.edit {
            it[PREFERENCE_KEY] = value
        }
    }
}
