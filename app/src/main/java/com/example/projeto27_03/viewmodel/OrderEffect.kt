package com.example.projeto27_03.viewmodel


// Efeitos de curta duração que a UI precisa consumir uma única vez.
// Eles não devem ficar no estado persistente da tela.
sealed class OrderEffect {
    // Mensagem genérica para snackbar.
    data class ShowSnackbar(val message:String): OrderEffect()
    // Feedback específico após carregar pedidos com sucesso.
    data class ShowOrdersLoaded(val count: Int): OrderEffect()
}