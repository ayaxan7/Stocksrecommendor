package com.ayaan.myapplication.ui.benchmark

import com.ayaan.myapplication.domain.GetBenchmarkAnalysisUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class BenchmarkAnalysisViewModel(
    private val getBenchmarkAnalysisUseCase: GetBenchmarkAnalysisUseCase,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    // StateFlow to expose UI state
    private val _state = MutableStateFlow<BenchmarkAnalysisState>(BenchmarkAnalysisState.Loading)
    val state: StateFlow<BenchmarkAnalysisState> = _state.asStateFlow()

    init {
        // Load data when ViewModel is created
        loadBenchmarkData()
    }

    /**
     * Loads benchmark analysis data from the repository
     */
    fun loadBenchmarkData() {
        coroutineScope.launch {
            _state.value = BenchmarkAnalysisState.Loading

            try {
                getBenchmarkAnalysisUseCase()
                    .onStart { _state.value = BenchmarkAnalysisState.Loading }
                    .catch { e ->
                        _state.value = BenchmarkAnalysisState.Error(e.message ?: "Unknown error occurred")
                    }
                    .collect { result ->
                        result.fold(
                            onSuccess = { benchmarkData ->
                                _state.value = BenchmarkAnalysisState.Success(benchmarkData)
                            },
                            onFailure = { e ->
                                _state.value = BenchmarkAnalysisState.Error(e.message ?: "Unknown error occurred")
                            }
                        )
                    }
            } catch (e: Exception) {
                _state.value = BenchmarkAnalysisState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    /**
     * Refresh benchmark data
     */
    fun refresh() {
        loadBenchmarkData()
    }
}
