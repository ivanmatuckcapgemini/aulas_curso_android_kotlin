package com.example.projeto27_03.viewmodel

import com.example.projeto27_03.data.Order

sealed class OrderEvent {
    object LoadOrders: OrderEvent()
    object Retry: OrderEvent()
    data class CreateOrder(val order: Order): OrderEvent()
}


