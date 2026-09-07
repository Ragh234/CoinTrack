package com.compose.cryptocurrency.domain.model

/**
 * currentValue/profitLoss/returnPercentage are computed only over holdings whose coin has a
 * cached price right now (hasPriceData == true on the holding). totalInvested still covers
 * every holding, so it can legitimately be larger than what currentValue accounts for while
 * unpricedHoldingsCount > 0 -- that gap is the unpriced holdings, not money that vanished.
 */
data class PortfolioSummary(
    val totalInvested: Double = 0.0,
    val currentValue: Double = 0.0,
    val profitLoss: Double = 0.0,
    val returnPercentage: Double = 0.0,
    val holdings: List<PortfolioHoldingSummary> = emptyList(),
    val unpricedHoldingsCount: Int = 0
)
