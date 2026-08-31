package com.compose.cryptocurrency.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.compose.cryptocurrency.data.local.entity.CachedCoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketDao {
    @Query("SELECT * FROM cached_coins ORDER BY rank ASC")
    fun observeCachedCoins(): Flow<List<CachedCoinEntity>>

    @Query("SELECT * FROM cached_coins ORDER BY rank ASC")
    suspend fun getCachedCoins(): List<CachedCoinEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCoins(coins: List<CachedCoinEntity>)

    @Query("DELETE FROM cached_coins")
    suspend fun clearCachedCoins()

    @Transaction
    suspend fun replaceCachedCoins(coins: List<CachedCoinEntity>) {
        clearCachedCoins()
        upsertCoins(coins)
    }
}
