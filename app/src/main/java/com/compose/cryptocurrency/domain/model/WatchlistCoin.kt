package com.compose.cryptocurrency.domain.model

data class WatchlistCoin(
    val coinId: String,
    val name: String,
    val symbol: String,
    val imageUrl: String?
)
