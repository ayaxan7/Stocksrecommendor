package com.ayaan.myapplication.network

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Function to build and configure the Ktor HttpClient
 */
fun createHttpClient(): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 60000 // 60 seconds
            connectTimeoutMillis = 30000 // 30 seconds
            socketTimeoutMillis = 60000  // 60 seconds
        }
    }
}

/**
 * Platform-specific base URL for API requests
 */
expect fun baseUrl(): String
