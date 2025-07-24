package com.ayaan.myapplication.ui.benchmark

import com.ayaan.myapplication.data.model.BenchmarkAnalysisResponse

/**
 * Represents the different states of the benchmark analysis UI
 */
sealed class BenchmarkAnalysisState {
    /**
     * Initial loading state when data is being fetched
     */
    data object Loading : BenchmarkAnalysisState()

    /**
     * Error state when data fetching fails
     */
    data class Error(val message: String) : BenchmarkAnalysisState()

    /**
     * Success state with loaded benchmark analysis data
     */
    data class Success(val benchmarkData: BenchmarkAnalysisResponse) : BenchmarkAnalysisState()
}
