package com.ayaan.myapplication.data

import com.ayaan.myapplication.data.model.FundMetrics
import com.ayaan.myapplication.network.FundApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository interface for fund data
 */
interface FundRepository {
    suspend fun getTop5SmallCapFunds(): Flow<Result<List<FundMetrics>>>
}

/**
 * Implementation of FundRepository that fetches data from FundApi
 */
class FundRepositoryImpl(private val fundApi: FundApi) : FundRepository {
    override suspend fun getTop5SmallCapFunds(): Flow<Result<List<FundMetrics>>> = flow {
        try {
            val funds = fundApi.getTop5SmallCap()
            emit(Result.success(funds))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
