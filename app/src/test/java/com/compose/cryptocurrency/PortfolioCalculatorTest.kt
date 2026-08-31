package com.compose.cryptocurrency

import com.compose.cryptocurrency.domain.model.PortfolioHolding
import com.compose.cryptocurrency.domain.usecase.portfolio.PortfolioCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class PortfolioCalculatorTest {

    @Test
    fun `calculateHolding returns profit and positive return`() {
        val holding = holding(quantity = 2.0, purchasePrice = 100.0)

        val result = PortfolioCalculator.calculateHolding(holding, currentPrice = 125.0)

        assertEquals(200.0, result.investedAmount, 0.0)
        assertEquals(250.0, result.currentValue, 0.0)
        assertEquals(50.0, result.profitLoss, 0.0)
        assertEquals(25.0, result.returnPercentage, 0.0)
    }

    @Test
    fun `calculateHolding returns loss and negative return`() {
        val holding = holding(quantity = 2.0, purchasePrice = 100.0)

        val result = PortfolioCalculator.calculateHolding(holding, currentPrice = 75.0)

        assertEquals(-50.0, result.profitLoss, 0.0)
        assertEquals(-25.0, result.returnPercentage, 0.0)
    }

    @Test
    fun `calculateHolding handles invalid invested amount`() {
        val holding = holding(quantity = 0.0, purchasePrice = 100.0)

        val result = PortfolioCalculator.calculateHolding(holding, currentPrice = 125.0)

        assertEquals(0.0, result.investedAmount, 0.0)
        assertEquals(0.0, result.currentValue, 0.0)
        assertEquals(0.0, result.returnPercentage, 0.0)
    }

    @Test
    fun `calculateSummary combines holdings`() {
        val first = PortfolioCalculator.calculateHolding(
            holding(quantity = 1.0, purchasePrice = 100.0),
            currentPrice = 120.0
        )
        val second = PortfolioCalculator.calculateHolding(
            holding(quantity = 2.0, purchasePrice = 50.0),
            currentPrice = 40.0
        )

        val result = PortfolioCalculator.calculateSummary(listOf(first, second))

        assertEquals(200.0, result.totalInvested, 0.0)
        assertEquals(200.0, result.currentValue, 0.0)
        assertEquals(0.0, result.profitLoss, 0.0)
        assertEquals(0.0, result.returnPercentage, 0.0)
    }

    private fun holding(quantity: Double, purchasePrice: Double) = PortfolioHolding(
        coinId = "btc-bitcoin",
        name = "Bitcoin",
        symbol = "BTC",
        quantity = quantity,
        purchasePrice = purchasePrice
    )
}
