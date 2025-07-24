package com.ayaan.myapplication.domain

import com.ayaan.myapplication.data.FundRepository
import com.ayaan.myapplication.data.model.BenchmarkAnalysisResponse
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving benchmark analysis data
 */
class GetBenchmarkAnalysisUseCase(private val repository: FundRepository) {
    suspend operator fun invoke(): Flow<Result<BenchmarkAnalysisResponse>> {
        return repository.getTop5SmallCapBenchmark()
    }
}
