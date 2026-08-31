package com.compose.cryptocurrency.presentation.coinlist

import com.compose.cryptocurrency.domain.model.Coin

data class CoinListState(
    val isLoading: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val searchQuery: String = "",
    val error: String = ""
)
