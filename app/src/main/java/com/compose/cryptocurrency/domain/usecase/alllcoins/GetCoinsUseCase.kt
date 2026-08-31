package com.compose.cryptocurrency.domain.usecase.alllcoins

import com.compose.cryptocurrency.common.Resource
import com.compose.cryptocurrency.domain.model.Coin
import com.compose.cryptocurrency.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(): Flow<Resource<List<Coin>>> = flow {
        try {
            emit(Resource.Loading())
            val coins = repository.getMarketCoins()
            repository.cacheMarketCoins(coins)
            emit(Resource.Success(coins))
        } catch (e: HttpException) {
            val cachedCoins = repository.getCachedMarketCoins()
            if (cachedCoins.isNotEmpty()) {
                emit(Resource.Success(cachedCoins))
            } else {
                emit(Resource.Error(e.localizedMessage ?: "Unable to load market data"))
            }
        } catch (e: IOException) {
            val cachedCoins = repository.getCachedMarketCoins()
            if (cachedCoins.isNotEmpty()) {
                emit(Resource.Success(cachedCoins))
            } else {
                emit(Resource.Error("Unable to load market data. Please check your connection."))
            }
        }
    }
}
