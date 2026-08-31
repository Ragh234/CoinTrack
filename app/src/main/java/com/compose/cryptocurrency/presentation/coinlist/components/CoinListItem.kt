package com.compose.cryptocurrency.presentation.coinlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.compose.cryptocurrency.domain.model.Coin
import com.compose.cryptocurrency.presentation.commonview.formatCompactInr
import com.compose.cryptocurrency.presentation.commonview.formatInr
import com.compose.cryptocurrency.presentation.commonview.formatPercent

@Composable
fun CoinListItem(
    coin: Coin, onItemClick: (Coin) -> Unit
) {

    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth().padding(start = 12.dp, end = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colors.background),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(coin) }
            .padding(20.dp),
            verticalAlignment = CenterVertically

        ) {
            CoinSymbolBadge(symbol = coin.symbol)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coin.name,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = MaterialTheme.colors.primaryVariant
                )
                Text(
                    text = "${coin.symbol.uppercase()} - ${formatCompactInr(coin.marketCap)} market cap",
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                Text(
                    text = formatInr(coin.currentPrice),
                    color = MaterialTheme.colors.primaryVariant,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = formatPercent(coin.percentChange24h),
                    color = if (coin.percentChange24h >= 0) Color(0xFF0F9D58) else MaterialTheme.colors.error,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.body2
                )
            }

        }
    }

}

@Composable
fun CoinSymbolBadge(symbol: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.primary.copy(alpha = 0.16f)),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = symbol.take(3).uppercase(),
            color = MaterialTheme.colors.primaryVariant,
            style = MaterialTheme.typography.caption
        )
    }
}
