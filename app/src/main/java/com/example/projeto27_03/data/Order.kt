package com.example.projeto27_03.data

// Modelo simples de domínio para representar um pedido na lista da tela principal.
// Mantê-lo como `data class` facilita cópias, comparações e exibição nos componentes Compose.
data class Order (
    val id:Int,
    val customerNmae:String,
    val total: Double,
)