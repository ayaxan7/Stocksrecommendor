package com.ayaan.myapplication.network

import com.ayaan.myapplication.BuildConfig.BASE_URL


/**
 * Android-specific implementation of baseUrl
 * Uses the remote server URL
 */
actual fun baseUrl(): String = BASE_URL
