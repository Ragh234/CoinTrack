package com.compose.cryptocurrency.presentation.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.compose.cryptocurrency.presentation.Screen
import com.compose.cryptocurrency.presentation.coinlist.components.CoinListItem

@Composable
fun WatchlistScreen(
    navController: NavController,
    viewModel: WatchlistViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp)
    ) {
        if (state.coins.isEmpty()) {
            Text(
                text = "Your watchlist is empty.",
                color = MaterialTheme.colors.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.coins) { item ->
                    CoinListItem(
                        coin = item.coin,
                        priceUnavailable = !item.hasPriceData,
                        onItemClick = { coin ->
                            navController.navigate(Screen.CoinDetailScreen.route + "/${coin.id}")
                        }
                    )
                }
            }
        }
    }
}
