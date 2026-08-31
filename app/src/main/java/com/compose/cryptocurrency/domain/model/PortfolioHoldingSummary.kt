package com.compose.cryptocurrency.domain.model

data class PortfolioHoldingSummary(
    val holding: PortfolioHolding,
    val currentPrice: Double,
    val investedAmount: Double,
    val currentValue: Double,
    val profitLoss: Double,
    val returnPercentage: Double
)
