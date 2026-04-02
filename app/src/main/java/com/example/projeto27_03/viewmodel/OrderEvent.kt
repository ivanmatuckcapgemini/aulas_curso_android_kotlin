package com.example.projeto27_03.viewmodel

import com.example.projeto27_03.data.Order

// Eventos que a tela pode disparar para o ViewModel.
// Isso ajuda a separar intenção do usuário da lógica de execução.
sealed class OrderEvent {
    // Solicita o carregamento da lista de pedidos.
    object LoadOrders: OrderEvent()
    // Reexecuta a busca depois de uma falha.
    object Retry: OrderEvent()
    // Exemplo de evento para criação de pedido, mantido para evolução do curso.
    data class CreateOrder(val order: Order): OrderEvent()
}


