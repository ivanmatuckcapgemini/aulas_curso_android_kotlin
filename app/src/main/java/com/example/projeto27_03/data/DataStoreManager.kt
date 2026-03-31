package com.example.projeto27_03.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Nome do DataStore usado para guardar a autenticação do usuário.
private const val AUTH_PREFS_NAME = "auth_prefs"

// Cria uma extensão do Context para acessar um DataStore do tipo Preferences.
// DataStore é assíncrono e mais seguro que SharedPreferences para esse tipo de estado simples.
private val Context.dataStore by preferencesDataStore(name = AUTH_PREFS_NAME)

class DataStoreManager(private val context: Context) {

    // Chave tipada que vai armazenar o token de autenticação.
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    // Salva o token retornado pelo backend/fonte de autenticação.
    // A operação é suspend porque DataStore grava de forma assíncrona.
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    // Remove o token salvo, efetivamente deslogando o usuário.
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    // Lê o token como Flow para que a interface reaja automaticamente a mudanças.
    // Se não existir token, o valor será null.
    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences: Preferences ->
            preferences[TOKEN_KEY]
        }
    }
}

