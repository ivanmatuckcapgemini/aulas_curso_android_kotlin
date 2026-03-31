package com.example.projeto27_03.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Nome do arquivo interno onde o DataStore vai persistir os valores.
private const val PREFS_NAME = "app_preferences"

// Responsável por centralizar a leitura e escrita de dados simples no armazenamento local.
// Agora usamos DataStore Preferences, que é a alternativa moderna e assíncrona ao SharedPreferences.
// Ele é assíncrono, baseado em Flow e mais seguro para estados pequenos do app.
private val Context.dataStore by preferencesDataStore(name = PREFS_NAME)

class PreferencesManager(private val context: Context) {
    // As chaves do DataStore são tipadas. Isso reduz erros de chave errada e melhora a leitura do código.
    private val isLoggedKey = booleanPreferencesKey(KEY_IS_LOGGED)
    private val userTypeKey = stringPreferencesKey(KEY_USER_TYPE)

    // Flow do estado de login.
    // Sempre que o valor for alterado, a UI que coleta esse Flow pode se recompor automaticamente.
    val loginStateFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[isLoggedKey] ?: false
    }

    // Flow do tipo de usuário salvo.
    // Se nada tiver sido gravado ainda, retornamos string vazia para não propagar null.
    val userTypeStateFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[userTypeKey] ?: DEFAULT_USER_TYPE
    }

    // Salva se o usuário está logado ou não.
    // DataStore usa coroutines, então essa operação é suspend para deixar a gravação assíncrona.
    suspend fun saveLoginState(isLogged: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[isLoggedKey] = isLogged
        }
    }

    // Salva o tipo de usuário selecionado ou autenticado.
    // Exemplo de uso: "admin", "cliente", "motorista" etc.
    suspend fun saveUserTypeState(userType: String) {
        context.dataStore.edit { preferences ->
            val normalizedUserType = userType.trim()

            if (normalizedUserType.isBlank()) {
                preferences.remove(userTypeKey)
            } else {
                preferences[userTypeKey] = normalizedUserType
            }
        }
    }

    // Remove a sessão do usuário de uma vez.
    // Isso é útil no logout, porque apaga tanto a marca de login quanto o perfil salvo.
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(isLoggedKey)
            preferences.remove(userTypeKey)
        }
    }

    companion object {
        // Chave usada para persistir se o usuário já está autenticado.
        private const val KEY_IS_LOGGED = "is_logged"
        // Chave usada para persistir o tipo/perfil do usuário.
        private const val KEY_USER_TYPE = "user_type"
        // Valor padrão caso ainda não exista nenhum tipo gravado.
        private const val DEFAULT_USER_TYPE = ""
    }
}


