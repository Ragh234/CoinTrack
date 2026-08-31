package com.compose.cryptocurrency.domain.usecase.watchlist

import com.compose.cryptocurrency.domain.model.Coin
import com.compose.cryptocurrency.domain.model.WatchlistCoin
import com.compose.cryptocurrency.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WatchlistUseCases @Inject constructor(
    private val repository: WatchlistRepository
) {
    fun observeWatchlist(): Flow<List<WatchlistCoin>> = repository.observeWatchlist()
    fun isInWatchlist(coinId: String): Flow<Boolean> = repository.isInWatchlist(coinId)
    suspend fun addCoin(coin: Coin) = repository.addCoin(coin)
    suspend fun removeCoin(coinId: String) = repository.removeCoin(coinId)
}
