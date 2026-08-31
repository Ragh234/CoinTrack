package com.compose.cryptocurrency.presentation.portfolio

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.compose.cryptocurrency.domain.model.PortfolioHoldingSummary
import com.compose.cryptocurrency.presentation.commonview.formatInr
import com.compose.cryptocurrency.presentation.commonview.formatPercent

@Composable
fun PortfolioScreen(
    viewModel: PortfolioViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            backgroundColor = MaterialTheme.colors.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Total Value",
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = formatInr(state.summary.currentValue),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.h4
                )
                Spacer(modifier = Modifier.height(12.dp))
                SummaryRow("Invested", formatInr(state.summary.totalInvested))
                SummaryRow("Profit/Loss", formatInr(state.summary.profitLoss))
                SummaryRow("Return", formatPercent(state.summary.returnPercentage))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Holdings",
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.h5
            )
            Button(onClick = viewModel::showAddHolding) {
                Text(text = "+ Add Holding", color = Color.White)
            }
        }

        if (state.summary.holdings.isEmpty()) {
            Text(
                text = "Add a holding to start tracking your portfolio.",
                color = MaterialTheme.colors.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp)
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.summary.holdings) { holding ->
                    HoldingItem(holding, onRemove = {
                        viewModel.removeHolding(holding.holding)
                    })
                }
            }
        }
    }

    if (state.showAddHolding) {
        AddHoldingDialog(viewModel = viewModel)
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = MaterialTheme.colors.secondary)
        Text(text = value, color = MaterialTheme.colors.primaryVariant)
    }
}

@Composable
private fun HoldingItem(holding: PortfolioHoldingSummary, onRemove: () -> Unit) {
    val profitColor = if (holding.profitLoss >= 0) Color(0xFF0F9D58) else MaterialTheme.colors.error
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = holding.holding.name,
                        color = MaterialTheme.colors.primaryVariant,
                        style = MaterialTheme.typography.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${holding.holding.quantity} ${holding.holding.symbol.uppercase()}",
                        color = MaterialTheme.colors.secondary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = formatInr(holding.currentValue), color = MaterialTheme.colors.primaryVariant)
                    Text(text = formatPercent(holding.returnPercentage), color = profitColor)
                }
            }
            TextButton(onClick = onRemove, modifier = Modifier.align(Alignment.End)) {
                Text(text = "Remove", color = MaterialTheme.colors.error)
            }
        }
    }
}

@Composable
private fun AddHoldingDialog(viewModel: PortfolioViewModel) {
    val state = viewModel.state.value
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = viewModel::dismissAddHolding,
        title = { Text(text = "Add Holding") },
        text = {
            Column {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = state.selectedCoin?.let { "${it.name} (${it.symbol.uppercase()})" }
                            ?: "Select cryptocurrency"
                    )
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    state.availableCoins.take(100).forEach { coin ->
                        DropdownMenuItem(onClick = {
                            viewModel.selectCoin(coin)
                            expanded = false
                        }) {
                            Text(text = "${coin.name} (${coin.symbol.uppercase()})")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.quantity,
                    onValueChange = viewModel::onQuantityChange,
                    label = { Text("Quantity") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.purchasePrice,
                    onValueChange = viewModel::onPurchasePriceChange,
                    label = { Text("Purchase price") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                state.formError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = viewModel::addHolding) {
                Text(text = "Save", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = viewModel::dismissAddHolding) {
                Text(text = "Cancel")
            }
        }
    )
}
