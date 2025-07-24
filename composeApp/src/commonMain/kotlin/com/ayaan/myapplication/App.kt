package com.ayaan.myapplication

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.ayaan.myapplication.ui.FundListScreen
import com.ayaan.myapplication.ui.FundListViewModel
import org.koin.compose.koinInject

@Composable
fun App() {
    MaterialTheme {
        // Inject the ViewModel using Koin
        val viewModel: FundListViewModel = koinInject()

        // Display the fund list screen
        FundListScreen(viewModel)
    }
}
