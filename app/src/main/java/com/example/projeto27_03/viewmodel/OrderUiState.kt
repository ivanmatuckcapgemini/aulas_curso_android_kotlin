package com.example.projeto27_03.viewmodel

import com.example.projeto27_03.data.Order

sealed class OrderUiState {
    object Idle: OrderUiState()
    object Loading: OrderUiState()
    data class Success(val orders:List<Order>): OrderUiState()
    data class Error(val message:String): OrderUiState()
}