package com.compose.cryptocurrency.presentation.watchlist

import com.compose.cryptocurrency.domain.usecase.watchlist.WatchlistDisplayItem

data class WatchlistState(
    val coins: List<WatchlistDisplayItem> = emptyList()
)
