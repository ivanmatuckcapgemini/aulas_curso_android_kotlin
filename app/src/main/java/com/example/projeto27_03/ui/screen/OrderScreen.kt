package com.example.projeto27_03.ui.screen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projeto27_03.R
import com.example.projeto27_03.data.Order
import com.example.projeto27_03.viewmodel.OrderEffect
import com.example.projeto27_03.viewmodel.OrderEvent
import com.example.projeto27_03.viewmodel.OrderUiState
import com.example.projeto27_03.viewmodel.OrderViewModel

// Função auxiliar para montar a mensagem de sucesso fora do bloco composable.
// Isso deixa o código mais legível e evita que a lógica de formatação fique misturada
// com a lógica de coleta dos efeitos da UI.
private fun buildOrdersLoadedMessage(context: Context, count: Int): String {
    return context.resources.getQuantityString(R.plurals.orders_loaded_success, count, count)
}

@Composable
fun OrderScreen(
    userType: String = "",
    onLogout: () -> Unit = {},
    viewModel: OrderViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when(effect){
                is OrderEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is OrderEffect.ShowOrdersLoaded -> {
                    snackbarHostState.showSnackbar(buildOrdersLoadedMessage(context, effect.count))
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Este bloco mostra o tipo de usuário recuperado das preferências.
            // Ele ajuda a visualizar o efeito do saveUserTypeState/getUserTypeState.
            if (userType.isNotBlank()) {
                Text(text = stringResource(R.string.saved_user_type, userType))
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Botão de saída: limpa o estado persistido e retorna a interface para o login.
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.logout_button))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onEvent(OrderEvent.LoadOrders) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Carregar Pedidos")
            }
            Spacer(modifier = Modifier.height(16.dp))

            when(state){
                OrderUiState.Idle ->{
                    Text("Clique para carregar os pedidos")
                }

                OrderUiState.Loading ->{
                    CircularProgressIndicator()
                    Text("Carregando pedidos...")
                }

                is OrderUiState.Success -> {
                    val orders = (state as OrderUiState.Success).orders
                    OrderList(orders)
                }

                is OrderUiState.Error -> {
                    val message = (state as OrderUiState.Error).message

                    Text("Erro: $message")

                    Button(
                        onClick = {viewModel.onEvent(OrderEvent.Retry)}
                    ) {
                        Text("Tentar Novamente")
                    }
                }
            }

        }
    }

}

@Composable
fun OrderList(orders: List<Order>) {
    LazyColumn {
        items(orders) {order ->
            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text("Cliente: ${order.customerNmae}")
                Text("Total: ${order.total}")
                HorizontalDivider()
            }
        }
    }




}