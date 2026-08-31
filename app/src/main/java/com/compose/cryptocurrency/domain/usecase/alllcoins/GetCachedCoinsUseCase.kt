package com.compose.cryptocurrency.domain.usecase.alllcoins

import com.compose.cryptocurrency.domain.model.Coin
import com.compose.cryptocurrency.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCachedCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(): Flow<List<Coin>> = repository.observeCachedMarketCoins()
}
