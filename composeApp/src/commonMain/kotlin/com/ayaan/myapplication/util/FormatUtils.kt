package com.ayaan.myapplication.util

import kotlin.math.pow
import kotlin.math.round

/**
 * Extension function to format Double as percentage with specified decimal places
 */
fun Double.formatPct(decimals: Int = 2): String {
    val factor = 10.0.pow(decimals)
    val rounded = round(this * factor) / factor
    return "${rounded}%"   // Append % sign
}
/**
 * Extension function to handle nullable sharpe ratio formatting
 * Returns formatted percentage or dash if null
 */
fun Double?.formatSharpeRatio(decimals: Int = 2): String {
    return this?.formatPct(decimals) ?: "—"
}
