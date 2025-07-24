package com.ayaan.myapplication.ui.funds

import com.ayaan.myapplication.domain.GetTopFundsUseCase
import com.ayaan.myapplication.ui.state.FundListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FundListViewModel(
    private val getTopFundsUseCase: GetTopFundsUseCase,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    // StateFlow to expose UI state
    private val _state = MutableStateFlow<FundListState>(FundListState.Loading)
    val state: StateFlow<FundListState> = _state.asStateFlow()

    init {
        // Load data when ViewModel is created
        loadFunds()
    }

    /**
     * Loads fund data from the repository
     */
    fun loadFunds() {
        coroutineScope.launch {
            _state.value = FundListState.Loading

            try {
                getTopFundsUseCase()
                    .onStart { _state.value = FundListState.Loading }
                    .catch { e ->
                        _state.value = FundListState.Error(e.message ?: "Unknown error occurred")
                    }
                    .collect { result ->
                        result.fold(
                            onSuccess = { funds ->
                                _state.value = if (funds.isEmpty()) {
                                    FundListState.Error("No funds found")
                                } else {
                                    FundListState.Success(funds)
                                }
                            },
                            onFailure = { e ->
                                _state.value = FundListState.Error(e.message ?: "Unknown error occurred")
                            }
                        )
                    }
            } catch (e: Exception) {
                _state.value = FundListState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    /**
     * Refresh fund data
     */
    fun refresh() {
        loadFunds()
    }
}
