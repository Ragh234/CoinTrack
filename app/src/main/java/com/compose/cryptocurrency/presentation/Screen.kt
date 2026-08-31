package com.compose.cryptocurrency.presentation

sealed class Screen(val route: String) {

    object CoinDetailScreen : Screen("coin_detail_screen")
}
