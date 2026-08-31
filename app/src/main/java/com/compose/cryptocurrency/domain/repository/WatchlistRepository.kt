package com.compose.cryptocurrency.domain.repository

import com.compose.cryptocurrency.domain.model.Coin
import com.compose.cryptocurrency.domain.model.WatchlistCoin
import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    fun observeWatchlist(): Flow<List<WatchlistCoin>>
    fun isInWatchlist(coinId: String): Flow<Boolean>
    suspend fun addCoin(coin: Coin)
    suspend fun removeCoin(coinId: String)
}
