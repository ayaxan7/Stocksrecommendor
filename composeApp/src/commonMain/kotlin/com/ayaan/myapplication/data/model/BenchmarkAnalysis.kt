package com.ayaan.myapplication.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BenchmarkAnalysisResponse(
    val funds: List<BenchmarkFundMetrics>,
    val benchmark: String,
    @SerialName("analysis_note") val analysisNote: String,
    @SerialName("recommendation_guide") val recommendationGuide: RecommendationGuide
)

@Serializable
data class BenchmarkFundMetrics(
    @SerialName("scheme_code") val schemeCode: Int,
    @SerialName("fund_name") val fundName: String,
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
    @SerialName("max_drawdown_pct") val maxDrawdownPct: Double? = null,
    val recommendation: String? = null
)

@Serializable
data class RecommendationGuide(
    @SerialName("STRONG BUY") val strongBuy: String,
    val BUY: String,
    val HOLD: String,
    val AVOID: String
)
