package com.compose.cryptocurrency.domain.usecase.coininfo

import com.compose.cryptocurrency.common.Resource
import com.compose.cryptocurrency.domain.model.CoinDetail
import com.compose.cryptocurrency.domain.repository.CoinRepository
import com.compose.cryptocurrency.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.io.IOException
import javax.inject.Inject

class GetCoinUseCase @Inject constructor(
    private val repository: CoinRepository,
    private val watchlistRepository: WatchlistRepository
) {
    operator fun invoke(coinId: String): Flow<Resource<CoinDetail>> = flow {
        try {
            emit(Resource.Loading())
            val ticker = repository.getTickerById(coinId)
            val quote = ticker.quotes?.get("INR") ?: ticker.quotes?.get("USD")
            val imageUrl = optionalRequest { repository.getCoinsById(coinId) }?.logo
            val history = optionalRequest {
                repository.getHistoricalPrices(coinId, thirtyDaysAgo())
            }?.mapNotNull { it.price }.orEmpty()
            val isInWatchlist = watchlistRepository.isInWatchlist(coinId).first()
            emit(
                Resource.Success(
                    CoinDetail(
                        coinId = ticker.id,
                        name = ticker.name,
                        symbol = ticker.symbol,
                        rank = ticker.rank,
                        currentPrice = quote?.price ?: 0.0,
                        percentChange24h = quote?.percentChange24h ?: 0.0,
                        marketCap = quote?.marketCap,
                        imageUrl = imageUrl,
                        chartPrices = history,
                        isInWatchlist = isInWatchlist
                    )
                )
            )

        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }

    private fun thirtyDaysAgo(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time)
    }

    private suspend fun <T> optionalRequest(request: suspend () -> T): T? = try {
        request()
    } catch (_: HttpException) {
        null
    } catch (_: IOException) {
        null
    }
}
