package com.ayaan.myapplication.domain

import com.ayaan.myapplication.data.FundRepository
import com.ayaan.myapplication.data.model.FundMetrics
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving top fund data
 */
class GetTopFundsUseCase(private val repository: FundRepository) {
    suspend operator fun invoke(): Flow<Result<List<FundMetrics>>> {
        return repository.getTop5SmallCapFunds()
    }
}
