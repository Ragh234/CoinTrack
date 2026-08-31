package com.compose.cryptocurrency.domain.model

data class PortfolioSummary(
    val totalInvested: Double = 0.0,
    val currentValue: Double = 0.0,
    val profitLoss: Double = 0.0,
    val returnPercentage: Double = 0.0,
    val holdings: List<PortfolioHoldingSummary> = emptyList()
)
