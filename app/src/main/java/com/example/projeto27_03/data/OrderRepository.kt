package com.example.projeto27_03.data

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class OrderRepository {
    suspend fun getOrders(shouldFail: Boolean): List<Order> {
        // Mantemos a simulação de rede, mas com uma espera menor para o app ficar mais leve
        // durante a navegação e a coleta de pedidos.
        delay(800L.milliseconds)

        if(shouldFail){
            throw Exception("Erro ao buscar pedido!")
        }
        return listOf(
            Order(1, "Katia Kotlin", 89.90),
            Order(2, "Constancia Compose",9.90),
            Order(3, "Vilson ViewModel", 12.0)
        )
    }
}