package com.ayaan.myapplication

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ayaan.myapplication.di.commonModule
import org.koin.core.context.startKoin

fun main() {
    // Initialize Koin
    startKoin {
        modules(commonModule)
    }

    // Start the desktop application
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Top 5 Small Cap Funds"
        ) {
            App()
        }
    }
}
