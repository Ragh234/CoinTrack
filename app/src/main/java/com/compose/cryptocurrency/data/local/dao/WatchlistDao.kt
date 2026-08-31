package com.compose.cryptocurrency.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.compose.cryptocurrency.data.local.entity.WatchlistCoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist_coins ORDER BY name ASC")
    fun observeWatchlist(): Flow<List<WatchlistCoinEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_coins WHERE coinId = :coinId)")
    fun isInWatchlist(coinId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCoin(coin: WatchlistCoinEntity)

    @Delete
    suspend fun removeCoin(coin: WatchlistCoinEntity)

    @Query("DELETE FROM watchlist_coins WHERE coinId = :coinId")
    suspend fun removeCoinById(coinId: String)
}
