package com.compose.cryptocurrency.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CoinDetailDto(
    @SerializedName("logo") val logo: String?
)
