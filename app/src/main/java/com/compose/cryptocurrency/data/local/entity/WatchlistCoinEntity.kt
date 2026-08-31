package com.compose.cryptocurrency.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.compose.cryptocurrency.domain.model.Coin
import com.compose.cryptocurrency.domain.model.WatchlistCoin

@Entity(tableName = "watchlist_coins")
data class WatchlistCoinEntity(
    @PrimaryKey val coinId: String,
    val name: String,
    val symbol: String,
    val imageUrl: String?
)

fun WatchlistCoinEntity.toWatchlistCoin(): WatchlistCoin = WatchlistCoin(
    coinId = coinId,
    name = name,
    symbol = symbol,
    imageUrl = imageUrl
)

fun Coin.toWatchlistCoinEntity(): WatchlistCoinEntity = WatchlistCoinEntity(
    coinId = id,
    name = name,
    symbol = symbol,
    imageUrl = imageUrl
)
