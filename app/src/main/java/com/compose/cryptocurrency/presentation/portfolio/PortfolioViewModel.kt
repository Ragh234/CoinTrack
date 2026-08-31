package com.compose.cryptocurrency.presentation.portfolio

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.cryptocurrency.domain.model.Coin
import com.compose.cryptocurrency.domain.model.PortfolioHolding
import com.compose.cryptocurrency.domain.usecase.portfolio.PortfolioUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val portfolioUseCases: PortfolioUseCases
) : ViewModel() {

    private val _state = mutableStateOf(PortfolioState())
    val state: State<PortfolioState> = _state

    init {
        portfolioUseCases.observeSummary().onEach { summary ->
            _state.value = _state.value.copy(summary = summary)
        }.launchIn(viewModelScope)

        portfolioUseCases.observeMarketCoins().onEach { coins ->
            _state.value = _state.value.copy(availableCoins = coins)
        }.launchIn(viewModelScope)
    }

    fun showAddHolding() {
        _state.value = _state.value.copy(showAddHolding = true, formError = null)
    }

    fun dismissAddHolding() {
        _state.value = _state.value.copy(
            showAddHolding = false,
            selectedCoin = null,
            quantity = "",
            purchasePrice = "",
            formError = null
        )
    }

    fun selectCoin(coin: Coin) {
        _state.value = _state.value.copy(selectedCoin = coin)
    }

    fun onQuantityChange(value: String) {
        _state.value = _state.value.copy(quantity = value)
    }

    fun onPurchasePriceChange(value: String) {
        _state.value = _state.value.copy(purchasePrice = value)
    }

    fun addHolding() {
        val coin = _state.value.selectedCoin
        val quantity = _state.value.quantity.toDoubleOrNull()
        val purchasePrice = _state.value.purchasePrice.toDoubleOrNull()

        when {
            coin == null -> _state.value = _state.value.copy(formError = "Select a cryptocurrency.")
            quantity == null || !quantity.isFinite() || quantity <= 0.0 ->
                _state.value = _state.value.copy(formError = "Quantity must be a finite number greater than 0.")
            purchasePrice == null || !purchasePrice.isFinite() || purchasePrice <= 0.0 ->
                _state.value = _state.value.copy(formError = "Purchase price must be a finite number greater than 0.")
            else -> viewModelScope.launch {
                portfolioUseCases.addHolding(
                    PortfolioHolding(
                        coinId = coin.id,
                        name = coin.name,
                        symbol = coin.symbol,
                        quantity = quantity,
                        purchasePrice = purchasePrice
                    )
                )
                dismissAddHolding()
            }
        }
    }

    fun removeHolding(holding: PortfolioHolding) {
        viewModelScope.launch {
            portfolioUseCases.removeHolding(holding)
        }
    }
}
