package com.ayaan.myapplication.ui.funds

import com.ayaan.myapplication.data.model.FundMetrics

/**
 * Represents the different states of the fund list UI
 */
sealed class FundListState {
    /**
     * Initial loading state when data is being fetched
     */
    data object Loading : FundListState()

    /**
     * Error state when data fetching fails
     */
    data class Error(val message: String) : FundListState()

    /**
     * Success state with loaded fund data
     */
    data class Success(val funds: List<FundMetrics>) : FundListState()
}