package com.ayaan.myapplication.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FundMetrics(
    @SerialName("scheme_code") val schemeCode: Int,
    @SerialName("fund_name") val fundName: String,
    @SerialName("recommendation") val recommendation: String="",
    @SerialName("returns_3m_pct") val returns3mPct: Double? = null,
    @SerialName("returns_6m_pct") val returns6mPct: Double? = null,
    @SerialName("returns_1y_pct") val returns1yPct: Double? = null,
    @SerialName("cagr_full_pct") val cagrFullPct: Double? = null,
    @SerialName("cagr_1y_pct") val cagr1yPct: Double? = null,
    @SerialName("cagr_2y_pct") val cagr2yPct: Double? = null,
    @SerialName("cagr_3y_pct") val cagr3yPct: Double? = null,
    @SerialName("cagr_5y_pct") val cagr5yPct: Double? = null,
    @SerialName("volatility_pct") val volatilityPct: Double? = null,
    @SerialName("sharpe_ratio") val sharpeRatio: Double? = null,
    @SerialName("max_drawdown_pct") val maxDrawdownPct: Double? = null
)

/**
 * Extension function to determine the quality category based on Sharpe ratio
 */
fun FundMetrics.getSharpeRatioCategory(): SharpeRatioCategory {
    return when {
        sharpeRatio == null -> SharpeRatioCategory.UNKNOWN
        sharpeRatio >= 1.0 -> SharpeRatioCategory.EXCELLENT
        sharpeRatio >= 0.5 -> SharpeRatioCategory.GOOD
        else -> SharpeRatioCategory.POOR
    }
}

/**
 * Enum to categorize funds based on their Sharpe ratio
 */
enum class SharpeRatioCategory {
    EXCELLENT,
    GOOD,
    POOR,
    UNKNOWN
}
