package com.compose.cryptocurrency.presentation.commonview

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

private val indianLocale = Locale("en", "IN")

fun formatInr(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(indianLocale)
    formatter.maximumFractionDigits = if (abs(value) >= 1000) 0 else 2
    return formatter.format(value)
}

fun formatCompactInr(value: Double?): String {
    value ?: return "N/A"
    return when {
        abs(value) >= 1_00_00_00_000 -> "${formatInr(value / 1_00_00_00_000)} Cr"
        abs(value) >= 1_00_000 -> "${formatInr(value / 1_00_000)} L"
        else -> formatInr(value)
    }
}

fun formatPercent(value: Double): String {
    val prefix = if (value > 0) "+" else ""
    return "$prefix${String.format(Locale.US, "%.2f", value)}%"
}
