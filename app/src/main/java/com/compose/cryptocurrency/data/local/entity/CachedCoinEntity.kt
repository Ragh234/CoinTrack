package com.compose.cryptocurrency.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.compose.cryptocurrency.domain.model.Coin

@Entity(tableName = "cached_coins")
data class CachedCoinEntity(
    @PrimaryKey val coinId: String,
    val name: String,
    val symbol: String,
    val rank: Int,
    val currentPrice: Double,
    val percentChange24h: Double,
    val marketCap: Double?,
    val imageUrl: String?
)

fun CachedCoinEntity.toCoin(): Coin = Coin(
    id = coinId,
    name = name,
    symbol = symbol,
    rank = rank,
    currentPrice = currentPrice,
    percentChange24h = percentChange24h,
    marketCap = marketCap,
    imageUrl = imageUrl
)

fun Coin.toCachedCoinEntity(): CachedCoinEntity = CachedCoinEntity(
    coinId = id,
    name = name,
    symbol = symbol,
    rank = rank,
    currentPrice = currentPrice,
    percentChange24h = percentChange24h,
    marketCap = marketCap,
    imageUrl = imageUrl
)
