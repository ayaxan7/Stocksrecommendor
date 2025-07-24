package com.ayaan.myapplication.ui.benchmark

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ayaan.myapplication.AppLogger
import com.ayaan.myapplication.ui.components.BenchmarkFundCard
import com.ayaan.myapplication.ui.components.RecommendationGuideCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun BenchmarkAnalysisScreen(viewModel: BenchmarkAnalysisViewModel) {
    val state by viewModel.state.collectAsState()
    val isRefreshing = state is BenchmarkAnalysisState.Loading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Benchmark Analysis") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            when (state) {
                is BenchmarkAnalysisState.Loading -> {
                    // Loading state is handled by the pull refresh indicator
                    if (!isRefreshing) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    AppLogger.d("BenchmarkAnalysisScreen", "Current state: $state")

                }

                is BenchmarkAnalysisState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (state as BenchmarkAnalysisState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.refresh() }) {
                            Text("Retry")
                        }
                    }
                    AppLogger.d("BenchmarkAnalysisScreen", "Current state: $state")

                }

                is BenchmarkAnalysisState.Success -> {
                    val data = (state as BenchmarkAnalysisState.Success).benchmarkData

                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            // Display benchmark info
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Benchmark: ${data.benchmark}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = data.analysisNote,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        // Fund cards
                        items(data.funds) { fund ->
                            BenchmarkFundCard(fund = fund)
                        }

                        // Recommendation guide
                        item {
                            RecommendationGuideCard(
                                strongBuy = data.recommendationGuide.strongBuy,
                                buy = data.recommendationGuide.BUY,
                                hold = data.recommendationGuide.HOLD,
                                avoid = data.recommendationGuide.AVOID
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(64.dp)) // Adjust height as needed
                        }
                    }
                    AppLogger.d("BenchmarkAnalysisScreen", "Current state: $state")
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
