package com.ayaan.myapplication.network

import com.ayaan.myapplication.data.model.ApiHealthResponse
import com.ayaan.myapplication.data.model.BenchmarkAnalysisResponse
import com.ayaan.myapplication.data.model.FundMetrics
import com.ayaan.myapplication.data.model.SmartAdvisorResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

/**
 * Interface defining the fund API endpoints
 */
interface FundApi {
    suspend fun getTop5SmallCap(): List<FundMetrics>
    suspend fun getTop5SmallCapBenchmark(): BenchmarkAnalysisResponse
    suspend fun getSmartAdvisor(
        riskTolerance: String,
        investmentHorizon: String,
        minAlpha: Double? = null,
        maxVolatility: Double? = null
    ): SmartAdvisorResponse

    /**
     * Checks if the API service is ready and responsive
     */
    suspend fun checkApiHealth(): ApiHealthResponse
}

/**
 * Implementation of the FundApi interface using Ktor client
 */
class FundApiImpl(private val client: HttpClient) : FundApi {
    override suspend fun getTop5SmallCap(): List<FundMetrics> {
        return client.get("${baseUrl()}/top5smallcap").body()
    }

    override suspend fun getTop5SmallCapBenchmark(): BenchmarkAnalysisResponse {
        return client.get("${baseUrl()}/top5smallcap-benchmark").body()
    }

    override suspend fun getSmartAdvisor(
        riskTolerance: String,
        investmentHorizon: String,
        minAlpha: Double?,
        maxVolatility: Double?
    ): SmartAdvisorResponse {
        return client.get("${baseUrl()}/smart-advisor") {
            parameter("risk_tolerance", riskTolerance)
            parameter("investment_horizon", investmentHorizon)
            minAlpha?.let { parameter("min_alpha", it) }
            maxVolatility?.let { parameter("max_volatility", it) }
        }.body()
    }

    override suspend fun checkApiHealth(): ApiHealthResponse {
        return client.get("${baseUrl()}/").body()
    }
}
