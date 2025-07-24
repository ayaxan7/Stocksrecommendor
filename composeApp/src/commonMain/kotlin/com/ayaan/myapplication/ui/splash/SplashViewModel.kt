package com.ayaan.myapplication.ui.splash

import com.ayaan.myapplication.domain.CheckApiHealthUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the splash screen that handles API health checking
 */
class SplashViewModel(
    private val checkApiHealthUseCase: CheckApiHealthUseCase,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    // StateFlow to expose UI state
    private val _state = MutableStateFlow<SplashState>(SplashState.Checking)
    val state: StateFlow<SplashState> = _state.asStateFlow()

    init {
        checkApiHealth()
    }

    /**
     * Checks if the API is ready by pinging the root endpoint
     */
    fun checkApiHealth() {
        _state.value = SplashState.Checking

        coroutineScope.launch {
            val isApiReady = checkApiHealthUseCase.waitUntilApiReady(
                maxAttempts = 15,  // Try up to 15 times
                delayMillis = 1500  // 1.5 second delay between attempts
            )

            if (isApiReady) {
                _state.value = SplashState.Ready
            } else {
                _state.value = SplashState.Error("API service is not responding. Please try again later.")
            }
        }
    }
}
