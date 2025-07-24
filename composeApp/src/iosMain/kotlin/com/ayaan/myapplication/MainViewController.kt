package com.ayaan.myapplication

import androidx.compose.ui.window.ComposeUIViewController
import com.ayaan.myapplication.di.commonModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

/**
 * Creates a UIViewController that displays our Compose UI for iOS
 */
fun MainViewController(): UIViewController {
    // Initialize Koin
    startKoin {
        modules(commonModule)
    }

    // Return a UIViewController hosting our Compose UI
    return ComposeUIViewController { App() }
}
