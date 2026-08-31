package com.compose.cryptocurrency.domain.model

data class PortfolioHolding(
    val id: Int = 0,
    val coinId: String,
    val name: String,
    val symbol: String,
    val quantity: Double,
    val purchasePrice: Double
)
