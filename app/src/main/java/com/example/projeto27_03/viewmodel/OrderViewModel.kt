package com.example.projeto27_03.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto27_03.data.Order
import com.example.projeto27_03.data.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    private val repository = OrderRepository()

    private val _uiState = MutableStateFlow<OrderUiState>(OrderUiState.Idle)
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    fun onEvent(event: OrderEvent){
        when(event){
            OrderEvent.LoadOrders -> loadOrders()
            OrderEvent.Retry -> loadOrders()
            is OrderEvent.CreateOrder -> {
//                addOrder()
            }
        }
    }

    private fun addOrder(order: Order){

    }
    private fun loadOrders(){
        viewModelScope.launch {
            _uiState.value = OrderUiState.Loading

            try{
                val orders = repository.getOrders(false)
                _uiState.value = OrderUiState.Success(orders)
            }catch (e: Exception){
                _uiState.value = OrderUiState.Error(
                    e.message ?: "Erro inesperado"
                )
            }
        }
    }














}