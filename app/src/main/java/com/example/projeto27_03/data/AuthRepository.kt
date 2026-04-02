package com.example.projeto27_03.data

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

// Repositório didático que simula uma autenticação remota.
// A ideia aqui é separar a regra de acesso a dados do ViewModel, deixando o fluxo
// mais próximo de uma arquitetura real, mas sem depender de backend externo.
class AuthRepository {
    // Executa a tentativa de login e devolve um `Result` para que a camada de UI
    // consiga tratar sucesso e erro sem precisar lidar com exceções fora do fluxo.
    suspend fun login(username: String, password: String): Result<String> {
        // Simula uma latência pequena de rede para manter o exemplo didático sem deixar
        // a UI com sensação de travamento.
        delay(800.milliseconds)

        // No cenário de estudo, validamos apenas um par fixo de credenciais.
        // Isso permite concentrar a aula no fluxo de estado, persistência e navegação.
        return if (username == "user" && password == "password") {
            Result.success("token_abc_123_456")
        } else {
            Result.failure(Exception("Credenciais inválidas"))
        }
    }
}

