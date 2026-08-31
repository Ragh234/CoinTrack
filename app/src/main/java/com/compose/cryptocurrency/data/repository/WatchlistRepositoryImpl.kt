package com.compose.cryptocurrency.data.repository

import com.compose.cryptocurrency.data.local.dao.WatchlistDao
import com.compose.cryptocurrency.data.local.entity.toWatchlistCoin
import com.compose.cryptocurrency.data.local.entity.toWatchlistCoinEntity
import com.compose.cryptocurrency.domain.model.Coin
import com.compose.cryptocurrency.domain.model.WatchlistCoin
import com.compose.cryptocurrency.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WatchlistRepositoryImpl @Inject constructor(
    private val watchlistDao: WatchlistDao
) : WatchlistRepository {
    override fun observeWatchlist(): Flow<List<WatchlistCoin>> =
        watchlistDao.observeWatchlist().map { coins -> coins.map { it.toWatchlistCoin() } }

    override fun isInWatchlist(coinId: String): Flow<Boolean> =
        watchlistDao.isInWatchlist(coinId)

    override suspend fun addCoin(coin: Coin) {
        watchlistDao.addCoin(coin.toWatchlistCoinEntity())
    }

    override suspend fun removeCoin(coinId: String) {
        watchlistDao.removeCoinById(coinId)
    }
}
