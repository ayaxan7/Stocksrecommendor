package com.ayaan.myapplication.network

import com.ayaan.myapplication.data.model.FundMetrics
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

/**
 * Interface defining the fund API endpoints
 */
interface FundApi {
    suspend fun getTop5SmallCap(): List<FundMetrics>
}

/**
 * Implementation of the FundApi interface using Ktor client
 */
class FundApiImpl(private val client: HttpClient) : FundApi {
    override suspend fun getTop5SmallCap(): List<FundMetrics> {
        return client.get("${baseUrl()}/top5smallcap").body()
    }
}
