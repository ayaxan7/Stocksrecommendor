package com.ayaan.myapplication.ui.smartadvisor

import com.ayaan.myapplication.domain.GetSmartAdvisorUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SmartAdvisorViewModel(
    private val getSmartAdvisorUseCase: GetSmartAdvisorUseCase,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    // StateFlow to expose UI state
    private val _state = MutableStateFlow<SmartAdvisorState>(SmartAdvisorState.Form)
    val state: StateFlow<SmartAdvisorState> = _state.asStateFlow()

    // Form input parameters
    private val _riskTolerance = MutableStateFlow("moderate")
    val riskTolerance: StateFlow<String> = _riskTolerance.asStateFlow()

    private val _investmentHorizon = MutableStateFlow("long_term")
    val investmentHorizon: StateFlow<String> = _investmentHorizon.asStateFlow()

    private val _minAlpha = MutableStateFlow<Double?>(0.0)
    val minAlpha: StateFlow<Double?> = _minAlpha.asStateFlow()

    private val _maxVolatility = MutableStateFlow<Double?>(25.0)
    val maxVolatility: StateFlow<Double?> = _maxVolatility.asStateFlow()

    /**
     * Updates the risk tolerance parameter
     */
    fun updateRiskTolerance(value: String) {
        _riskTolerance.value = value
    }

    /**
     * Updates the investment horizon parameter
     */
    fun updateInvestmentHorizon(value: String) {
        _investmentHorizon.value = value
    }

    /**
     * Updates the minimum alpha parameter
     */
    fun updateMinAlpha(value: Double?) {
        _minAlpha.value = value
    }

    /**
     * Updates the maximum volatility parameter
     */
    fun updateMaxVolatility(value: Double?) {
        _maxVolatility.value = value
    }

    /**
     * Gets advisor recommendations based on current parameters
     */
    fun getRecommendations() {
        coroutineScope.launch {
            _state.value = SmartAdvisorState.Loading

            try {
                getSmartAdvisorUseCase(
                    riskTolerance = _riskTolerance.value,
                    investmentHorizon = _investmentHorizon.value,
                    minAlpha = _minAlpha.value,
                    maxVolatility = _maxVolatility.value
                )
                    .onStart { _state.value = SmartAdvisorState.Loading }
                    .catch { e ->
                        _state.value = SmartAdvisorState.Error(e.message ?: "Unknown error occurred")
                    }
                    .collect { result ->
                        result.fold(
                            onSuccess = { advisorData ->
                                _state.value = SmartAdvisorState.Success(advisorData)
                            },
                            onFailure = { e ->
                                _state.value = SmartAdvisorState.Error(e.message ?: "Unknown error occurred")
                            }
                        )
                    }
            } catch (e: Exception) {
                _state.value = SmartAdvisorState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    /**
     * Resets to form state
     */
    fun resetToForm() {
        _state.value = SmartAdvisorState.Form
    }
}
