package com.compose.cryptocurrency.domain.usecase.watchlist

import com.compose.cryptocurrency.domain.model.Coin
import com.compose.cryptocurrency.domain.model.WatchlistCoin

/**
 * A watchlisted coin paired with whether it currently has a real market price. hasPriceData
 * is false when the coin isn't in the cached market list -- before the market screen has
 * loaded once, offline, or the coin has dropped out of the latest ticker response.
 *
 * coin.currentPrice/percentChange24h/marketCap are 0.0/0.0/null in that case, same as before
 * this existed: they are placeholder values to satisfy Coin's shape, not real data. Anything
 * reading them must check hasPriceData first, the same rule PortfolioHoldingSummary follows
 * for the identical situation on the Portfolio screen.
 */
data class WatchlistDisplayItem(
    val coin: Coin,
    val hasPriceData: Boolean
)

object WatchlistDisplayMapper {

    fun merge(watchlist: List<WatchlistCoin>, cachedCoins: List<Coin>): List<WatchlistDisplayItem> {
        val cachedById = cachedCoins.associateBy { it.id }
        return watchlist.map { saved ->
            val cached = cachedById[saved.coinId]
            if (cached != null) {
                WatchlistDisplayItem(coin = cached, hasPriceData = true)
            } else {
                WatchlistDisplayItem(
                    coin = Coin(
                        id = saved.coinId,
                        name = saved.name,
                        symbol = saved.symbol,
                        rank = 0,
                        currentPrice = 0.0,
                        percentChange24h = 0.0,
                        marketCap = null,
                        imageUrl = saved.imageUrl
                    ),
                    hasPriceData = false
                )
            }
        }
    }
}
