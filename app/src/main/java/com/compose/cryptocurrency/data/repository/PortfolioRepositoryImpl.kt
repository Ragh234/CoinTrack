package com.compose.cryptocurrency.data.repository

import com.compose.cryptocurrency.data.local.dao.PortfolioDao
import com.compose.cryptocurrency.data.local.entity.toPortfolioHolding
import com.compose.cryptocurrency.data.local.entity.toPortfolioHoldingEntity
import com.compose.cryptocurrency.domain.model.PortfolioHolding
import com.compose.cryptocurrency.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PortfolioRepositoryImpl @Inject constructor(
    private val portfolioDao: PortfolioDao
) : PortfolioRepository {
    override fun observeHoldings(): Flow<List<PortfolioHolding>> =
        portfolioDao.observeHoldings().map { holdings -> holdings.map { it.toPortfolioHolding() } }

    override suspend fun addHolding(holding: PortfolioHolding) {
        portfolioDao.addHolding(holding.toPortfolioHoldingEntity())
    }

    override suspend fun removeHolding(holding: PortfolioHolding) {
        portfolioDao.removeHolding(holding.toPortfolioHoldingEntity())
    }
}
