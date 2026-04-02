package com.example.projeto27_03.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto27_03.data.Order
import com.example.projeto27_03.data.OrderRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    // O repositório concentra a fonte dos dados; o ViewModel apenas coordena estados e efeitos.
    private val repository = OrderRepository()

    // Estado principal da tela: idle, loading, sucesso ou erro.
    private val _uiState = MutableStateFlow<OrderUiState>(OrderUiState.Idle)
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    // Efeitos são eventos de uso único, como snackbar, e não devem ficar “presos” no estado.
    private val _effect = MutableSharedFlow<OrderEffect>()
    val effect = _effect.asSharedFlow()


    fun onEvent(event: OrderEvent){
        // Centraliza o roteamento de ações da tela para manter o fluxo fácil de estudar.
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
            // Atualizamos a UI antes da chamada assíncrona para mostrar loading imediatamente.
            _uiState.value = OrderUiState.Loading

            try{
                val orders = repository.getOrders(false)
                // Em caso de sucesso, trocamos o estado e emitimos um efeito de feedback.
                _uiState.value = OrderUiState.Success(orders)
                _effect.emit(OrderEffect.ShowOrdersLoaded(orders.size))
            }catch (e: Exception){

                // Em caso de falha, o estado leva a mensagem para a tela e o efeito mostra snackbar.
                _uiState.value = OrderUiState.Error(
                    e.message ?: "Erro inesperado"
                )
                _effect.emit(OrderEffect.ShowSnackbar("Falha ao carregar pedidos"))

            }
        }
    }














}