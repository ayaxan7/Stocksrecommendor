package com.ayaan.myapplication.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SmartAdvisorResponse(
    @SerialName("recommended_funds") val recommendedFunds: List<RecommendedFundMetrics> = emptyList(),
    @SerialName("analysis_summary") val analysisSummary: AnalysisSummary = AnalysisSummary(),
    @SerialName("investment_strategy") val investmentStrategy: InvestmentStrategy = InvestmentStrategy(),
    @SerialName("portfolio_allocation") val portfolioAllocation: PortfolioAllocation = PortfolioAllocation(),
    @SerialName("market_outlook") val marketOutlook: String = "",
    @SerialName("risk_warnings") val riskWarnings: List<String> = emptyList(),
    @SerialName("filters_applied") val filtersApplied: FiltersApplied = FiltersApplied()
)

@Serializable
data class RecommendedFundMetrics(
    @SerialName("scheme_code") val schemeCode: Int = 0,
    @SerialName("fund_name") val fundName: String = "",
    @SerialName("alpha_pct") val alphaPct: Double? = null,
    @SerialName("volatility_pct") val volatilityPct: Double? = null,
    @SerialName("risk_score") val riskScore: Int? = null,
    @SerialName("sharpe_ratio") val sharpeRatio: Double? = null,
    val recommendation: String? = null
)

@Serializable
data class AnalysisSummary(
    @SerialName("total_funds_analyzed") val totalFundsAnalyzed: Int? = 0,
    @SerialName("funds_with_positive_alpha") val fundsWithPositiveAlpha: Int? = 0,
    @SerialName("average_alpha_pct") val averageAlphaPct: Double? = null,
    @SerialName("average_volatility_pct") val averageVolatilityPct: Double? = null,
    @SerialName("market_outperformance") val marketOutperformance: String? = ""
)

@Serializable
data class InvestmentStrategy(
    @SerialName("primary_recommendation") val primaryRecommendation: String = "",
    val strategy: String = "",
    @SerialName("horizon_note") val horizonNote: String = ""
)

@Serializable
data class PortfolioAllocation(
    @SerialName("small_cap_funds") val smallCapFunds: String = "",
    @SerialName("large_cap_funds") val largeCapFunds: String = "",
    val recommendation: String = "",
    @SerialName("top_fund_suggestion") val topFundSuggestion: String = ""
)

@Serializable
data class FiltersApplied(
    @SerialName("risk_tolerance") val riskTolerance: String = "moderate",
    @SerialName("investment_horizon") val investmentHorizon: String = "long_term",
    @SerialName("min_alpha_pct") val minAlphaPct: Double = 0.0,
    @SerialName("max_volatility_pct") val maxVolatilityPct: Double = 25.0
)
