package com.compose.cryptocurrency.domain.model

data class CoinDetail(
    val coinId: String,
    val name: String,
    val symbol: String,
    val rank: Int,
    val currentPrice: Double,
    val percentChange24h: Double,
    val marketCap: Double?,
    val imageUrl: String?,
    val chartPrices: List<Double>,
    val isInWatchlist: Boolean = false
)
