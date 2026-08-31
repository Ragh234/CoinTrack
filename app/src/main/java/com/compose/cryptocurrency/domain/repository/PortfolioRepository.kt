package com.compose.cryptocurrency.domain.repository

import com.compose.cryptocurrency.domain.model.PortfolioHolding
import kotlinx.coroutines.flow.Flow

interface PortfolioRepository {
    fun observeHoldings(): Flow<List<PortfolioHolding>>
    suspend fun addHolding(holding: PortfolioHolding)
    suspend fun removeHolding(holding: PortfolioHolding)
}
