package com.compose.cryptocurrency

import com.compose.cryptocurrency.domain.model.Coin
import com.compose.cryptocurrency.domain.model.WatchlistCoin
import com.compose.cryptocurrency.domain.usecase.watchlist.WatchlistDisplayMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WatchlistDisplayMapperTest {

    @Test
    fun `a watchlisted coin present in the cache is marked as having price data`() {
        val watchlist = listOf(WatchlistCoin("btc-bitcoin", "Bitcoin", "BTC", null))
        val cached = listOf(coin("btc-bitcoin", price = 7_000_000.0))

        val result = WatchlistDisplayMapper.merge(watchlist, cached)

        assertEquals(1, result.size)
        assertTrue(result[0].hasPriceData)
        assertEquals(7_000_000.0, result[0].coin.currentPrice, 0.0)
    }

    /**
     * The bug this pins: a watchlisted coin missing from the cache used to be shown with a
     * fabricated price of 0.0 and 0% change, indistinguishable from a coin that genuinely
     * costs nothing.
     */
    @Test
    fun `a watchlisted coin missing from the cache is marked as unpriced, not zero`() {
        val watchlist = listOf(WatchlistCoin("delisted-coin", "Delisted Coin", "DEL", null))

        val result = WatchlistDisplayMapper.merge(watchlist, cachedCoins = emptyList())

        assertEquals(1, result.size)
        assertFalse(result[0].hasPriceData)
        assertEquals("delisted-coin", result[0].coin.id)
    }

    @Test
    fun `mixed watchlist keeps priced and unpriced entries in original order`() {
        val watchlist = listOf(
            WatchlistCoin("btc-bitcoin", "Bitcoin", "BTC", null),
            WatchlistCoin("delisted-coin", "Delisted Coin", "DEL", null)
        )
        val cached = listOf(coin("btc-bitcoin", price = 7_000_000.0))

        val result = WatchlistDisplayMapper.merge(watchlist, cached)

        assertEquals(listOf(true, false), result.map { it.hasPriceData })
    }

    private fun coin(id: String, price: Double) = Coin(
        id = id,
        name = id,
        symbol = id,
        rank = 1,
        currentPrice = price,
        percentChange24h = 0.0,
        marketCap = null
    )
}
