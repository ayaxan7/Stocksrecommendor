package com.ayaan.myapplication.domain

import com.ayaan.myapplication.network.FundApi
import kotlinx.coroutines.delay

/**
 * Use case for checking API health and waiting until the service is ready
 */
class CheckApiHealthUseCase(private val fundApi: FundApi) {
    /**
     * Checks if the API is ready by repeatedly pinging the root endpoint
     * until it receives a {"message":"OK"} response
     *
     * @param maxAttempts Maximum number of attempts before giving up
     * @param delayMillis Delay between attempts in milliseconds
     * @return True if the API is ready, false otherwise
     */
    suspend fun waitUntilApiReady(maxAttempts: Int = 10, delayMillis: Long = 2000): Boolean {
        var attempts = 0

        while (attempts < maxAttempts) {
            try {
                val response = fundApi.checkApiHealth()
                if (response.message == "OK") {
                    return true
                }
            } catch (e: Exception) {
                // API is not ready yet, continue trying
            }

            attempts++
            delay(delayMillis)
        }

        return false
    }
}
