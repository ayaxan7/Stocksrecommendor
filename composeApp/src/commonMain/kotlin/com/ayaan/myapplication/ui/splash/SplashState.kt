package com.ayaan.myapplication.ui.splash

/**
 * Represents the different states of the splash screen
 */
sealed class SplashState {
    /**
     * Initial state when checking API health
     */
    data object Checking : SplashState()

    /**
     * API is ready and we can proceed to the main app
     */
    data object Ready : SplashState()

    /**
     * API check failed after maximum attempts
     */
    data class Error(val message: String) : SplashState()
}
