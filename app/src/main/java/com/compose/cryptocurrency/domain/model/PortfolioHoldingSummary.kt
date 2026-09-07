package com.compose.cryptocurrency.domain.model

/**
 * currentPrice/currentValue/profitLoss/returnPercentage are null when the holding's coin has
 * no cached market price yet (first launch before the market screen loads, offline, or the
 * coin dropped out of the latest ticker list). Treating that as a known price of 0 used to
 * render every such holding as a silent 100% loss.
 */
data class PortfolioHoldingSummary(
    val holding: PortfolioHolding,
    val currentPrice: Double?,
    val investedAmount: Double,
    val currentValue: Double?,
    val profitLoss: Double?,
    val returnPercentage: Double?,
    val hasPriceData: Boolean
)
