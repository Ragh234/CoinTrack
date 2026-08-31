package com.compose.cryptocurrency.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.compose.cryptocurrency.data.local.entity.PortfolioHoldingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {
    @Query("SELECT * FROM portfolio_holdings ORDER BY name ASC")
    fun observeHoldings(): Flow<List<PortfolioHoldingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHolding(holding: PortfolioHoldingEntity)

    @Delete
    suspend fun removeHolding(holding: PortfolioHoldingEntity)
}
