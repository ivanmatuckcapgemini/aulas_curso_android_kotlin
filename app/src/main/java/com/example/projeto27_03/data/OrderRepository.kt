package com.example.projeto27_03.data

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

// Repositório fictício usado para simular a busca de pedidos.
// Ele existe para demonstrar carregamento assíncrono, sucesso e erro sem backend real.
class OrderRepository {
    // Busca os pedidos com atraso artificial para evidenciar o estado de loading na UI.
    suspend fun getOrders(shouldFail: Boolean): List<Order> {
        // Mantemos a simulação de rede, mas com uma espera menor para o app ficar mais leve
        // durante a navegação e a coleta de pedidos.
        delay(800L.milliseconds)

        // O erro opcional ajuda no estudo do fluxo de retry e da exibição de mensagens.
        if(shouldFail){
            throw Exception("Erro ao buscar pedido!")
        }

        // Retornamos uma lista fixa para que o foco fique na renderização da lista.
        return listOf(
            Order(1, "Katia Kotlin", 89.90),
            Order(2, "Constancia Compose",9.90),
            Order(3, "Vilson ViewModel", 12.0)
        )
    }
}