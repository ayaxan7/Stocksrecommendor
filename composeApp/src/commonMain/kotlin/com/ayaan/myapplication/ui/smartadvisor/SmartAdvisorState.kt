package com.ayaan.myapplication.ui.smartadvisor

import com.ayaan.myapplication.data.model.SmartAdvisorResponse

/**
 * Represents the different states of the smart advisor UI
 */
sealed class SmartAdvisorState {
    /**
     * Initial loading state when data is being fetched
     */
    data object Loading : SmartAdvisorState()

    /**
     * Form input state before recommendations are loaded
     */
    data object Form : SmartAdvisorState()

    /**
     * Error state when data fetching fails
     */
    data class Error(val message: String) : SmartAdvisorState()

    /**
     * Success state with loaded smart advisor recommendations
     */
    data class Success(val advisorData: SmartAdvisorResponse) : SmartAdvisorState()
}
