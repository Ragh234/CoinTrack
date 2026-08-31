package com.compose.cryptocurrency.data.remote.dto

import com.compose.cryptocurrency.domain.model.Coin
import com.google.gson.annotations.SerializedName

data class TickerDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("rank") val rank: Int,
    @SerializedName("quotes") val quotes: Map<String, QuoteDto>?
)

data class QuoteDto(
    @SerializedName("price") val price: Double?,
    @SerializedName("market_cap") val marketCap: Double?,
    @SerializedName("percent_change_24h") val percentChange24h: Double?
)

fun TickerDto.toCoin(imageUrl: String? = null): Coin {
    val quote = quotes?.get("INR") ?: quotes?.get("USD")
    return Coin(
        id = id,
        name = name,
        symbol = symbol,
        rank = rank,
        currentPrice = quote?.price ?: 0.0,
        percentChange24h = quote?.percentChange24h ?: 0.0,
        marketCap = quote?.marketCap,
        imageUrl = imageUrl
    )
}
