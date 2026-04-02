package com.example.projeto27_03.viewmodel

import com.example.projeto27_03.data.Order

// Estado de exibição da lista de pedidos.
// Mantém a tela simples de renderizar e deixa claro o que a UI deve mostrar em cada fase.
sealed class OrderUiState {
    object Idle: OrderUiState()
    object Loading: OrderUiState()
    data class Success(val orders:List<Order>): OrderUiState()
    data class Error(val message:String): OrderUiState()
}