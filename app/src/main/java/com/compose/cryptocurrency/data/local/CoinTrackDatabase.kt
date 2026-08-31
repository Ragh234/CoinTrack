package com.compose.cryptocurrency.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.compose.cryptocurrency.data.local.dao.MarketDao
import com.compose.cryptocurrency.data.local.dao.PortfolioDao
import com.compose.cryptocurrency.data.local.dao.WatchlistDao
import com.compose.cryptocurrency.data.local.entity.CachedCoinEntity
import com.compose.cryptocurrency.data.local.entity.PortfolioHoldingEntity
import com.compose.cryptocurrency.data.local.entity.WatchlistCoinEntity

@Database(
    entities = [
        CachedCoinEntity::class,
        WatchlistCoinEntity::class,
        PortfolioHoldingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CoinTrackDatabase : RoomDatabase() {
    abstract fun marketDao(): MarketDao
    abstract fun watchlistDao(): WatchlistDao
    abstract fun portfolioDao(): PortfolioDao
}
