package com.compose.cryptocurrency.presentation.watchlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.cryptocurrency.domain.usecase.alllcoins.GetCachedCoinsUseCase
import com.compose.cryptocurrency.domain.usecase.watchlist.WatchlistDisplayMapper
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
                WatchlistDisplayMapper.merge(watchlist, cachedCoins)
            }
            .onEach { coins -> _state.value = WatchlistState(coins) }
            .launchIn(viewModelScope)
    }
}
