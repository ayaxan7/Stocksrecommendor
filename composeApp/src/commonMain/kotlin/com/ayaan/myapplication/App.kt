package com.ayaan.myapplication

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.ayaan.myapplication.ui.funds.FundListScreen
import com.ayaan.myapplication.ui.funds.FundListViewModel
import com.ayaan.myapplication.ui.benchmark.BenchmarkAnalysisScreen
import com.ayaan.myapplication.ui.benchmark.BenchmarkAnalysisViewModel
import com.ayaan.myapplication.ui.smartadvisor.SmartAdvisorScreen
import com.ayaan.myapplication.ui.smartadvisor.SmartAdvisorViewModel
import com.ayaan.myapplication.ui.splash.SplashScreen
import com.ayaan.myapplication.ui.splash.SplashViewModel
import org.koin.compose.koinInject

enum class AppScreen {
    FUND_LIST,
    BENCHMARK_ANALYSIS,
    SMART_ADVISOR
}

@Composable
fun App() {
    MaterialTheme {
        var showSplashScreen by remember { mutableStateOf(true) }
        var currentScreen by remember { mutableStateOf(AppScreen.FUND_LIST) }

        if (showSplashScreen) {
            // Show splash screen until API is ready
            val splashViewModel: SplashViewModel = koinInject()
            SplashScreen(
                viewModel = splashViewModel,
                onApiReady = { showSplashScreen = false }
            )
        } else {
            // Show main app content after API is ready
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Fund List") },
                            label = { Text("Funds") },
                            selected = currentScreen == AppScreen.FUND_LIST,
                            onClick = { currentScreen = AppScreen.FUND_LIST }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Analytics, contentDescription = "Benchmark Analysis") },
                            label = { Text("Benchmark") },
                            selected = currentScreen == AppScreen.BENCHMARK_ANALYSIS,
                            onClick = { currentScreen = AppScreen.BENCHMARK_ANALYSIS }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.SmartToy, contentDescription = "Smart Advisor") },
                            label = { Text("Advisor") },
                            selected = currentScreen == AppScreen.SMART_ADVISOR,
                            onClick = { currentScreen = AppScreen.SMART_ADVISOR }
                        )
                    }
                }
            ) { paddingValues ->
                when (currentScreen) {
                    AppScreen.FUND_LIST -> {
                        val viewModel: FundListViewModel = koinInject()
                        FundListScreen(viewModel = viewModel)
                    }

                    AppScreen.BENCHMARK_ANALYSIS -> {
                        val viewModel: BenchmarkAnalysisViewModel = koinInject()
                        BenchmarkAnalysisScreen(viewModel = viewModel)
                    }

                    AppScreen.SMART_ADVISOR -> {
                        val viewModel: SmartAdvisorViewModel = koinInject()
                        SmartAdvisorScreen(
                            viewModel = viewModel,
                            onNavigateBack = { currentScreen = AppScreen.FUND_LIST }
                        )
                    }
                }
            }
        }
    }
}
