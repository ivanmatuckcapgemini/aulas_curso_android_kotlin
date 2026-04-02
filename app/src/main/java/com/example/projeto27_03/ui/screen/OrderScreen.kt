package com.example.projeto27_03.ui.screen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderScreen(
    userType: String = "",
    onLogout: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    viewModel: OrderViewModel = viewModel()
) {
    // A tela observa estado e efeitos separadamente: um para renderização contínua e outro para mensagens pontuais.
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // SnackbarHostState permite exibir feedback sem poluir o estado principal da tela.
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Efeitos são coletados uma única vez para evitar repetição de mensagens em recomposição.
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
    ) { contentPadding ->
        // O conteúdo principal também é rolável para acomodar lista, botões e mensagens.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Exibe o tipo de usuário salvo para reforçar o uso de DataStore no login.
            if (userType.isNotBlank()) {
                Text(text = stringResource(R.string.saved_user_type, userType))
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Botão de saída: limpa o token e devolve o fluxo à tela de login.
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.logout_button))
            }

            // Atalho para a tela de configurações, onde o aluno explora tema e perfil de cores.
            Button(
                onClick = onOpenSettings,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.open_settings_button))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onEvent(OrderEvent.LoadOrders) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Carregar Pedidos")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // O when reflete o ciclo de vida da lista: parado, carregando, sucesso ou erro.
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
    // A lista é mostrada em coluna porque a tela principal já está rolável como um todo.
    Column {
        orders.forEach { order ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Cliente: ${order.customerNmae}")
                Text("Total: ${order.total}")
                HorizontalDivider()
            }
        }
    }

}