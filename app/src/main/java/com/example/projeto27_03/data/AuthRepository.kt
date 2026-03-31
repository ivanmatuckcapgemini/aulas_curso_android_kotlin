package com.example.projeto27_03.data

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

// Simula uma fonte remota de autenticação.
// Em um app real, aqui você chamaria uma API e receberia um token JWT ou semelhante.
class AuthRepository {
    suspend fun login(username: String, password: String): Result<String> {
        // Simula uma latência pequena de rede para manter o exemplo didático sem deixar
        // a UI com sensação de travamento.
        delay(800.milliseconds)

        return if (username == "user" && password == "password") {
            Result.success("token_abc_123_456")
        } else {
            Result.failure(Exception("Credenciais inválidas"))
        }
    }
}

