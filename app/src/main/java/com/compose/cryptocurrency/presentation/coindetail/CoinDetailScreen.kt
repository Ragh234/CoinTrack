package com.compose.cryptocurrency.presentation.coindetail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.compose.cryptocurrency.presentation.coinlist.ErrorState
import com.compose.cryptocurrency.presentation.coinlist.components.CoinSymbolBadge
import com.compose.cryptocurrency.presentation.commonview.formatCompactInr
import com.compose.cryptocurrency.presentation.commonview.formatInr
import com.compose.cryptocurrency.presentation.commonview.formatPercent

@Composable
fun CoinDetailScreen(
    viewModel: CoinDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        when {
            state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            state.error.isNotEmpty() -> ErrorState(
                message = state.error,
                onRetry = viewModel::retry,
                modifier = Modifier.align(Alignment.Center)
            )
            state.coin != null -> {
                val coin = state.coin
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (coin.imageUrl.isNullOrBlank()) {
                            CoinSymbolBadge(symbol = coin.symbol, modifier = Modifier.size(58.dp))
                        } else {
                            AsyncImage(
                                model = coin.imageUrl,
                                contentDescription = coin.name,
                                modifier = Modifier.size(58.dp)
                            )
                        }
                        Column(modifier = Modifier.padding(start = 14.dp).weight(1f)) {
                            Text(
                                text = coin.name,
                                style = MaterialTheme.typography.h4,
                                color = MaterialTheme.colors.primaryVariant
                            )
                            Text(
                                text = "${coin.symbol.uppercase()} - Rank ${coin.rank}",
                                color = MaterialTheme.colors.secondary
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = MaterialTheme.colors.surface
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            DetailRow("Current price", formatInr(coin.currentPrice))
                            DetailRow("24h change", formatPercent(coin.percentChange24h))
                            DetailRow("Market cap", formatCompactInr(coin.marketCap))
                        }
                    }

                    Text(
                        text = "Price chart",
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.primaryVariant
                    )
                    PriceChart(prices = coin.chartPrices)

                    Button(
                        onClick = viewModel::toggleWatchlist,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (state.isInWatchlist) "Remove from Watchlist" else "Add to Watchlist",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = MaterialTheme.colors.secondary)
        Text(text = value, color = MaterialTheme.colors.primaryVariant, textAlign = TextAlign.End)
    }
}

@Composable
private fun PriceChart(prices: List<Double>) {
    if (prices.size < 2) {
        Text(
            text = "Chart data is not available right now.",
            color = MaterialTheme.colors.secondary,
            modifier = Modifier.padding(vertical = 24.dp)
        )
        return
    }

    val lineColor = if (prices.last() >= prices.first()) Color(0xFF0F9D58) else Color(0xFFDB4437)
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(MaterialTheme.colors.surface, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        val min = prices.minOrNull() ?: 0.0
        val max = prices.maxOrNull() ?: 0.0
        val range = (max - min).takeIf { it > 0.0 } ?: 1.0
        val stepX = size.width / (prices.lastIndex.coerceAtLeast(1))
        val path = Path()
        prices.forEachIndexed { index, price ->
            val x = index * stepX
            val y = size.height - (((price - min) / range).toFloat() * size.height)
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawLine(
            color = Color.LightGray.copy(alpha = 0.25f),
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = 2f
        )
        drawPath(path = path, color = lineColor, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5f))
    }
}
