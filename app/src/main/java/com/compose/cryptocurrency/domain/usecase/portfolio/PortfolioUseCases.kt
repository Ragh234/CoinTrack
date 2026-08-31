package com.compose.cryptocurrency.domain.usecase.portfolio

import com.compose.cryptocurrency.domain.model.Coin
import com.compose.cryptocurrency.domain.model.PortfolioHolding
import com.compose.cryptocurrency.domain.model.PortfolioSummary
import com.compose.cryptocurrency.domain.repository.CoinRepository
import com.compose.cryptocurrency.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class PortfolioUseCases @Inject constructor(
    private val portfolioRepository: PortfolioRepository,
    private val coinRepository: CoinRepository
) {
    fun observeSummary(): Flow<PortfolioSummary> {
        return portfolioRepository.observeHoldings()
            .combine(coinRepository.observeCachedMarketCoins()) { holdings, coins ->
                val prices = coins.associateBy { it.id }
                val summaries = holdings.map { holding ->
                    PortfolioCalculator.calculateHolding(
                        holding = holding,
                        currentPrice = prices[holding.coinId]?.currentPrice ?: 0.0
                    )
                }
                PortfolioCalculator.calculateSummary(summaries)
            }
    }

    fun observeMarketCoins(): Flow<List<Coin>> = coinRepository.observeCachedMarketCoins()

    suspend fun addHolding(holding: PortfolioHolding) = portfolioRepository.addHolding(holding)

    suspend fun removeHolding(holding: PortfolioHolding) = portfolioRepository.removeHolding(holding)
}
