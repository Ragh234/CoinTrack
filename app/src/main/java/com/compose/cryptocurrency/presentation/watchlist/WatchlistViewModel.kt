package com.compose.cryptocurrency.presentation.watchlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.cryptocurrency.domain.model.Coin
import com.compose.cryptocurrency.domain.usecase.alllcoins.GetCachedCoinsUseCase
import com.compose.cryptocurrency.domain.usecase.watchlist.WatchlistUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    watchlistUseCases: WatchlistUseCases,
    getCachedCoinsUseCase: GetCachedCoinsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(WatchlistState())
    val state: State<WatchlistState> = _state

    init {
        watchlistUseCases.observeWatchlist()
            .combine(getCachedCoinsUseCase()) { watchlist, cachedCoins ->
                val cachedById = cachedCoins.associateBy { it.id }
                watchlist.map { saved ->
                    cachedById[saved.coinId] ?: Coin(
                        id = saved.coinId,
                        name = saved.name,
                        symbol = saved.symbol,
                        rank = 0,
                        currentPrice = 0.0,
                        percentChange24h = 0.0,
                        marketCap = null,
                        imageUrl = saved.imageUrl
                    )
                }
            }
            .onEach { coins -> _state.value = WatchlistState(coins) }
            .launchIn(viewModelScope)
    }
}
