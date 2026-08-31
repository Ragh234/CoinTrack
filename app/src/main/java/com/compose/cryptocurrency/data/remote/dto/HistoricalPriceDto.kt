package com.compose.cryptocurrency.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HistoricalPriceDto(
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("price") val price: Double?,
    @SerializedName("market_cap") val marketCap: Double?
)
