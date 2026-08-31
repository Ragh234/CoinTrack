package com.compose.cryptocurrency.presentation.coindetail

import com.compose.cryptocurrency.domain.model.CoinDetail

data class CoinDetailState(
    val isLoading: Boolean = false,
    val coin: CoinDetail? = null,
    val isInWatchlist: Boolean = false,
    val error: String = ""
)
