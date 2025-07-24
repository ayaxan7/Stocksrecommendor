package com.ayaan.myapplication.data

import com.ayaan.myapplication.App
import com.ayaan.myapplication.AppLogger
import com.ayaan.myapplication.data.model.BenchmarkAnalysisResponse
import com.ayaan.myapplication.data.model.FundMetrics
import com.ayaan.myapplication.data.model.SmartAdvisorResponse
import com.ayaan.myapplication.network.FundApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository interface for fund data
 */
interface FundRepository {
    suspend fun getTop5SmallCapFunds(): Flow<Result<List<FundMetrics>>>

    suspend fun getTop5SmallCapBenchmark(): Flow<Result<BenchmarkAnalysisResponse>>

    suspend fun getSmartAdvisor(
        riskTolerance: String,
        investmentHorizon: String,
        minAlpha: Double? = null,
        maxVolatility: Double? = null
    ): Flow<Result<SmartAdvisorResponse>>
}

/**
 * Implementation of FundRepository that fetches data from FundApi
 */
class FundRepositoryImpl(private val fundApi: FundApi) : FundRepository {
    override suspend fun getTop5SmallCapFunds(): Flow<Result<List<FundMetrics>>> = flow {
        try {
            val funds = fundApi.getTop5SmallCap()
            emit(Result.success(funds))
            AppLogger.d("FundRepositoryImpl", "Top 5 Small Cap Funds: $funds")
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getTop5SmallCapBenchmark(): Flow<Result<BenchmarkAnalysisResponse>> = flow {
        try {
            val benchmarkData = fundApi.getTop5SmallCapBenchmark()
            emit(Result.success(benchmarkData))
            AppLogger.d("FundRepositoryImpl", "Top 5 Small Cap Benchmark Data: $benchmarkData")
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getSmartAdvisor(
        riskTolerance: String,
        investmentHorizon: String,
        minAlpha: Double?,
        maxVolatility: Double?
    ): Flow<Result<SmartAdvisorResponse>> = flow {
        try {
            val advisorData = fundApi.getSmartAdvisor(
                riskTolerance = riskTolerance,
                investmentHorizon = investmentHorizon,
                minAlpha = minAlpha,
                maxVolatility = maxVolatility
            )
            emit(Result.success(advisorData))
            AppLogger.d("FundRepositoryImpl", "Smart Advisor Data: $advisorData")
//            Log.d("FundRepositoryImpl", "Smart Advisor Data: $advisorData")
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
