package com.compose.cryptocurrency.domain.usecase.portfolio

import com.compose.cryptocurrency.domain.model.PortfolioHolding
import com.compose.cryptocurrency.domain.model.PortfolioHoldingSummary
import com.compose.cryptocurrency.domain.model.PortfolioSummary

object PortfolioCalculator {

    /**
     * @param currentPrice null or non-positive means "no cached price for this coin right now",
     * not "this coin is worth zero." Those are very different facts for a holding's owner, and
     * collapsing them used to render every unpriced holding as a 100% loss.
     */
    fun calculateHolding(
        holding: PortfolioHolding,
        currentPrice: Double?
    ): PortfolioHoldingSummary {
        val investedAmount = validAmount(holding.quantity * holding.purchasePrice)
        val hasPriceData = currentPrice != null && currentPrice.isFinite() && currentPrice > 0.0

        if (!hasPriceData) {
            return PortfolioHoldingSummary(
                holding = holding,
                currentPrice = null,
                investedAmount = investedAmount,
                currentValue = null,
                profitLoss = null,
                returnPercentage = null,
                hasPriceData = false
            )
        }

        val currentValue = validAmount(holding.quantity * currentPrice!!)
        val profitLoss = currentValue - investedAmount
        val returnPercentage = if (investedAmount > 0.0) {
            (profitLoss / investedAmount) * 100
        } else {
            0.0
        }

        return PortfolioHoldingSummary(
            holding = holding,
            currentPrice = currentPrice,
            investedAmount = investedAmount,
            currentValue = currentValue,
            profitLoss = profitLoss,
            returnPercentage = returnPercentage,
            hasPriceData = true
        )
    }

    fun calculateSummary(holdings: List<PortfolioHoldingSummary>): PortfolioSummary {
        val priced = holdings.filter { it.hasPriceData }
        val totalInvested = holdings.sumOf { it.investedAmount }
        val pricedInvested = priced.sumOf { it.investedAmount }
        val currentValue = priced.sumOf { it.currentValue ?: 0.0 }
        val profitLoss = currentValue - pricedInvested
        val returnPercentage = if (pricedInvested > 0.0) {
            (profitLoss / pricedInvested) * 100
        } else {
            0.0
        }

        return PortfolioSummary(
            totalInvested = totalInvested,
            currentValue = currentValue,
            profitLoss = profitLoss,
            returnPercentage = returnPercentage,
            holdings = holdings,
            unpricedHoldingsCount = holdings.size - priced.size
        )
    }

    private fun validAmount(value: Double): Double =
        if (value.isFinite() && value > 0.0) value else 0.0
}
