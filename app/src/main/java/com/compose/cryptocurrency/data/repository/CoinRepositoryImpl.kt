package com.compose.cryptocurrency.data.repository

import com.compose.cryptocurrency.data.local.dao.MarketDao
import com.compose.cryptocurrency.data.local.entity.toCachedCoinEntity
import com.compose.cryptocurrency.data.local.entity.toCoin
import com.compose.cryptocurrency.data.remote.CyptoCurrencyApi
import com.compose.cryptocurrency.data.remote.dto.CoinDetailDto
import com.compose.cryptocurrency.data.remote.dto.HistoricalPriceDto
import com.compose.cryptocurrency.data.remote.dto.TickerDto
import com.compose.cryptocurrency.data.remote.dto.toCoin
import com.compose.cryptocurrency.domain.model.Coin
import com.compose.cryptocurrency.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CyptoCurrencyApi,
    private val marketDao: MarketDao
) : CoinRepository {
    override suspend fun getCoinsById(coinId: String): CoinDetailDto {
        return api.getCoinById(coinId)
    }

    override suspend fun getMarketCoins(): List<Coin> {
        return api.getTickers("INR").map { it.toCoin() }
    }

    override suspend fun getCachedMarketCoins(): List<Coin> {
        return marketDao.getCachedCoins().map { it.toCoin() }
    }

    override fun observeCachedMarketCoins(): Flow<List<Coin>> {
        return marketDao.observeCachedCoins().map { coins -> coins.map { it.toCoin() } }
    }

    override suspend fun getTickerById(coinId: String): TickerDto {
        return api.getTickerById(coinId, "INR")
    }

    override suspend fun getHistoricalPrices(
        coinId: String,
        start: String
    ): List<HistoricalPriceDto> {
        return api.getHistoricalPrices(coinId = coinId, start = start)
    }

    override suspend fun cacheMarketCoins(coins: List<Coin>) {
        marketDao.replaceCachedCoins(coins.map { it.toCachedCoinEntity() })
    }
}
