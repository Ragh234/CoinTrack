package com.compose.cryptocurrency.domain.usecase.portfolio

import com.compose.cryptocurrency.domain.model.PortfolioHolding
import com.compose.cryptocurrency.domain.model.PortfolioHoldingSummary
import com.compose.cryptocurrency.domain.model.PortfolioSummary

object PortfolioCalculator {

    fun calculateHolding(
        holding: PortfolioHolding,
        currentPrice: Double
    ): PortfolioHoldingSummary {
        val investedAmount = validAmount(holding.quantity * holding.purchasePrice)
        val currentValue = validAmount(holding.quantity * currentPrice)
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
            returnPercentage = returnPercentage
        )
    }

    fun calculateSummary(holdings: List<PortfolioHoldingSummary>): PortfolioSummary {
        val totalInvested = holdings.sumOf { it.investedAmount }
        val currentValue = holdings.sumOf { it.currentValue }
        val profitLoss = currentValue - totalInvested
        val returnPercentage = if (totalInvested > 0.0) {
            (profitLoss / totalInvested) * 100
        } else {
            0.0
        }

        return PortfolioSummary(
            totalInvested = totalInvested,
            currentValue = currentValue,
            profitLoss = profitLoss,
            returnPercentage = returnPercentage,
            holdings = holdings
        )
    }

    private fun validAmount(value: Double): Double =
        if (value.isFinite() && value > 0.0) value else 0.0
}
