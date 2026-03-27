package com.example.projeto27_03.data

import kotlinx.coroutines.delay

class OrderRepository {
    suspend fun getOrders(shouldFail: Boolean): List<Order> {
        delay(2000)

        if(shouldFail){
            throw Exception("Erro ao buscar pedido!")
        }
        return listOf(
            Order(1, "Katia Kotlin", 89.90),
            Order(2, "Constancia Compose",9.90),
            Order(2, "Vilson ViewModel", 12.0)
        )
    }
}