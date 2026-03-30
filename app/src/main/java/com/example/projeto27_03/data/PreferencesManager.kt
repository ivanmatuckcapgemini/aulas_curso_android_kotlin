package com.example.projeto27_03.data

import android.content.Context
import androidx.core.content.edit

// Responsável por centralizar a leitura e escrita de dados simples no armazenamento local.
// Aqui usamos SharedPreferences, que é indicado para valores pequenos, como flags booleanas
// e textos curtos (por exemplo: "usuário logado" e "tipo de usuário").
class PreferencesManager(context: Context) {
    // Abre o arquivo de preferências do app.
    // Context.MODE_PRIVATE garante que somente este aplicativo tenha acesso a esses dados.
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Salva se o usuário está logado ou não.
    // Esse valor pode ser usado, por exemplo, para decidir se a tela inicial será login ou home.
    fun saveLoginState(isLogged: Boolean) {
        prefs.edit {
            putBoolean(KEY_IS_LOGGED, isLogged)
        }
    }

    // Lê o estado de login salvo anteriormente.
    // Se ainda não existir nada gravado, o valor padrão será 'false'.
    fun getLoginState(): Boolean = prefs.getBoolean(KEY_IS_LOGGED, false)

    // Salva o tipo de usuário selecionado ou autenticado.
    // Exemplo de uso: "admin", "cliente", "motorista" etc.
    fun saveUserTypeState(userType: String) {
        prefs.edit {
            putString(KEY_USER_TYPE, userType)
        }
    }

    // Lê o tipo de usuário salvo.
    // Se a chave ainda não existir, retornamos uma string vazia para evitar null no restante do código.
    fun getUserTypeState(): String = prefs.getString(KEY_USER_TYPE, DEFAULT_USER_TYPE) ?: DEFAULT_USER_TYPE

    // Limpa os dados da sessão em um único lugar.
    // Isso é útil no logout, porque remove tanto a marca de login quanto o perfil salvo.
    fun clearSession() {
        prefs.edit {
            remove(KEY_IS_LOGGED)
            remove(KEY_USER_TYPE)
        }
    }

    companion object {
        // Nome do arquivo interno onde os valores serão armazenados.
        private const val PREFS_NAME = "app_preferences"
        // Chave usada para persistir se o usuário já está autenticado.
        private const val KEY_IS_LOGGED = "is_logged"
        // Chave usada para persistir o tipo/perfil do usuário.
        private const val KEY_USER_TYPE = "user_type"
        // Valor padrão caso ainda não exista nenhum tipo gravado.
        private const val DEFAULT_USER_TYPE = ""
    }
}


