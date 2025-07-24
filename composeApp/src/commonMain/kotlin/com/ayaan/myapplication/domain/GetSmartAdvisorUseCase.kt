package com.ayaan.myapplication.domain

import com.ayaan.myapplication.data.FundRepository
import com.ayaan.myapplication.data.model.SmartAdvisorResponse
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving smart advisor recommendations
 */
class GetSmartAdvisorUseCase(private val repository: FundRepository) {
    suspend operator fun invoke(
        riskTolerance: String,
        investmentHorizon: String,
        minAlpha: Double? = null,
        maxVolatility: Double? = null
    ): Flow<Result<SmartAdvisorResponse>> {
        return repository.getSmartAdvisor(
            riskTolerance = riskTolerance,
            investmentHorizon = investmentHorizon,
            minAlpha = minAlpha,
            maxVolatility = maxVolatility
        )
    }
}
