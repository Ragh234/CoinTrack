package com.compose.cryptocurrency.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.compose.cryptocurrency.domain.model.PortfolioHolding

@Entity(tableName = "portfolio_holdings")
data class PortfolioHoldingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coinId: String,
    val name: String,
    val symbol: String,
    val quantity: Double,
    val purchasePrice: Double
)

fun PortfolioHoldingEntity.toPortfolioHolding(): PortfolioHolding = PortfolioHolding(
    id = id,
    coinId = coinId,
    name = name,
    symbol = symbol,
    quantity = quantity,
    purchasePrice = purchasePrice
)

fun PortfolioHolding.toPortfolioHoldingEntity(): PortfolioHoldingEntity = PortfolioHoldingEntity(
    id = id,
    coinId = coinId,
    name = name,
    symbol = symbol,
    quantity = quantity,
    purchasePrice = purchasePrice
)
