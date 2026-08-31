package com.compose.cryptocurrency.data.remote

import com.compose.cryptocurrency.data.remote.dto.CoinDetailDto
import com.compose.cryptocurrency.data.remote.dto.HistoricalPriceDto
import com.compose.cryptocurrency.data.remote.dto.TickerDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CyptoCurrencyApi {

    @GET(ApiConstant.API_GET_COIN_DETAIL+ "{coinId}")
    suspend fun getCoinById(@Path(ApiConstant.COIN_ID) coinId: String): CoinDetailDto

    @GET(ApiConstant.API_GET_TICKERS)
    suspend fun getTickers(@Query("quotes") quotes: String = "INR"): List<TickerDto>

    @GET(ApiConstant.API_GET_TICKER_DETAIL + "{coinId}")
    suspend fun getTickerById(
        @Path(ApiConstant.COIN_ID) coinId: String,
        @Query("quotes") quotes: String = "INR"
    ): TickerDto

    @GET(ApiConstant.API_GET_HISTORICAL)
    suspend fun getHistoricalPrices(
        @Path(ApiConstant.COIN_ID) coinId: String,
        @Query("start") start: String,
        @Query("interval") interval: String = "1d",
        @Query("quote") quote: String = "usd"
    ): List<HistoricalPriceDto>
}
