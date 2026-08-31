package com.compose.cryptocurrency.domain.repository

import com.compose.cryptocurrency.data.remote.dto.CoinDetailDto
import com.compose.cryptocurrency.data.remote.dto.HistoricalPriceDto
import com.compose.cryptocurrency.data.remote.dto.TickerDto
import com.compose.cryptocurrency.domain.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getCoinsById(coinId: String): CoinDetailDto
    suspend fun getMarketCoins(): List<Coin>
    suspend fun getCachedMarketCoins(): List<Coin>
    fun observeCachedMarketCoins(): Flow<List<Coin>>
    suspend fun getTickerById(coinId: String): TickerDto
    suspend fun getHistoricalPrices(coinId: String, start: String): List<HistoricalPriceDto>
    suspend fun cacheMarketCoins(coins: List<Coin>)
}
