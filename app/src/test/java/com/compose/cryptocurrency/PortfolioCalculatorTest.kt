package com.compose.cryptocurrency

import com.compose.cryptocurrency.domain.model.PortfolioHolding
import com.compose.cryptocurrency.domain.usecase.portfolio.PortfolioCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PortfolioCalculatorTest {

    @Test
    fun `calculateHolding returns profit and positive return`() {
        val holding = holding(quantity = 2.0, purchasePrice = 100.0)

        val result = PortfolioCalculator.calculateHolding(holding, currentPrice = 125.0)

        assertTrue(result.hasPriceData)
        assertEquals(200.0, result.investedAmount, 0.0)
        assertEquals(250.0, result.currentValue!!, 0.0)
        assertEquals(50.0, result.profitLoss!!, 0.0)
        assertEquals(25.0, result.returnPercentage!!, 0.0)
    }

    @Test
    fun `calculateHolding returns loss and negative return`() {
        val holding = holding(quantity = 2.0, purchasePrice = 100.0)

        val result = PortfolioCalculator.calculateHolding(holding, currentPrice = 75.0)

        assertEquals(-50.0, result.profitLoss!!, 0.0)
        assertEquals(-25.0, result.returnPercentage!!, 0.0)
    }

    @Test
    fun `calculateHolding handles invalid invested amount`() {
        val holding = holding(quantity = 0.0, purchasePrice = 100.0)

        val result = PortfolioCalculator.calculateHolding(holding, currentPrice = 125.0)

        assertEquals(0.0, result.investedAmount, 0.0)
        assertEquals(0.0, result.currentValue!!, 0.0)
        assertEquals(0.0, result.returnPercentage!!, 0.0)
    }

    /**
     * The bug this pins: a holding whose coin has no cached market price right now (first
     * launch before the market screen loads, offline, or the coin dropped out of the latest
     * ticker list) used to be priced at 0.0 by default, which rendered as a silent 100% loss.
     * A missing price and a real price of zero are different facts and must not collapse
     * into the same result.
     */
    @Test
    fun `calculateHolding with no cached price reports unpriced, not a loss`() {
        val holding = holding(quantity = 2.0, purchasePrice = 100.0)

        val result = PortfolioCalculator.calculateHolding(holding, currentPrice = null)

        assertFalse(result.hasPriceData)
        assertEquals(200.0, result.investedAmount, 0.0)
        assertNull(result.currentPrice)
        assertNull(result.currentValue)
        assertNull(result.profitLoss)
        assertNull(result.returnPercentage)
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
        assertEquals(0, result.unpricedHoldingsCount)
    }

    /**
     * The unpriced holding's invested amount still counts toward totalInvested (the user did
     * spend that money), but it must not drag currentValue/profitLoss down as if it were
     * worth zero -- that was the exact shape of the bug.
     */
    @Test
    fun `calculateSummary excludes unpriced holdings from value and return, but not from invested`() {
        val priced = PortfolioCalculator.calculateHolding(
            holding(quantity = 1.0, purchasePrice = 100.0),
            currentPrice = 120.0
        )
        val unpriced = PortfolioCalculator.calculateHolding(
            holding(quantity = 5.0, purchasePrice = 200.0),
            currentPrice = null
        )

        val result = PortfolioCalculator.calculateSummary(listOf(priced, unpriced))

        assertEquals(1100.0, result.totalInvested, 0.0) // 100 + 1000, both holdings
        assertEquals(120.0, result.currentValue, 0.0)   // only the priced holding
        assertEquals(20.0, result.profitLoss, 0.0)      // computed against priced invested only
        assertEquals(1, result.unpricedHoldingsCount)
    }

    private fun holding(quantity: Double, purchasePrice: Double) = PortfolioHolding(
        coinId = "btc-bitcoin",
        name = "Bitcoin",
        symbol = "BTC",
        quantity = quantity,
        purchasePrice = purchasePrice
    )
}
