package com.compose.cryptocurrency.presentation.portfolio

import com.compose.cryptocurrency.domain.model.Coin
import com.compose.cryptocurrency.domain.model.PortfolioSummary

data class PortfolioState(
    val summary: PortfolioSummary = PortfolioSummary(),
    val availableCoins: List<Coin> = emptyList(),
    val showAddHolding: Boolean = false,
    val selectedCoin: Coin? = null,
    val quantity: String = "",
    val purchasePrice: String = "",
    val formError: String? = null
)
