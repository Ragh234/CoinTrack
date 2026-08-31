package com.compose.cryptocurrency.presentation.coindetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.cryptocurrency.common.AppConstants
import com.compose.cryptocurrency.common.Resource
import com.compose.cryptocurrency.domain.model.Coin
import com.compose.cryptocurrency.domain.usecase.coininfo.GetCoinUseCase
import com.compose.cryptocurrency.domain.usecase.watchlist.WatchlistUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val getCoinUseCase: GetCoinUseCase,
    private val watchlistUseCases: WatchlistUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(CoinDetailState())
    val state: State<CoinDetailState> = _state
    private var currentCoinId: String? = null

    init {
        savedStateHandle.get<String>(AppConstants.COIN_PARAM_ID)?.let { coinId ->
            currentCoinId = coinId
            getCoin(coinId = coinId)
        }
    }

    private fun getCoin(coinId: String) {
        getCoinUseCase(coinId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = CoinDetailState(
                        isLoading = false,
                        coin = result.data,
                        isInWatchlist = result.data?.isInWatchlist ?: false
                    )

                }
                is Resource.Error -> {
                    _state.value = CoinDetailState(
                        isLoading = false, error = result.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Loading -> {
                    _state.value = CoinDetailState(isLoading = true)

                }
            }
        }.launchIn(viewModelScope)
    }

    fun toggleWatchlist() {
        val coinDetail = _state.value.coin ?: return
        viewModelScope.launch {
            if (_state.value.isInWatchlist) {
                watchlistUseCases.removeCoin(coinDetail.coinId)
                _state.value = _state.value.copy(isInWatchlist = false)
            } else {
                watchlistUseCases.addCoin(
                    Coin(
                        id = coinDetail.coinId,
                        name = coinDetail.name,
                        symbol = coinDetail.symbol,
                        rank = coinDetail.rank,
                        currentPrice = coinDetail.currentPrice,
                        percentChange24h = coinDetail.percentChange24h,
                        marketCap = coinDetail.marketCap,
                        imageUrl = coinDetail.imageUrl
                    )
                )
                _state.value = _state.value.copy(isInWatchlist = true)
            }
        }
    }

    fun retry() {
        currentCoinId?.let { getCoin(it) }
    }
}
