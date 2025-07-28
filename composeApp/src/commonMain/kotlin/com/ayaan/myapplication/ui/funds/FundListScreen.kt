package com.ayaan.myapplication.ui.funds

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ayaan.myapplication.data.model.FundMetrics
import com.ayaan.myapplication.data.model.SharpeRatioCategory
import com.ayaan.myapplication.data.model.getSharpeRatioCategory
import com.ayaan.myapplication.ui.funds.FundListState
import com.ayaan.myapplication.util.formatPct
import com.ayaan.myapplication.util.formatSharpeRatio
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import com.ayaan.myapplication.AppLogger
import com.ayaan.myapplication.ui.components.MetricItem

enum class SortOption {
    CAGR_5Y,
    CAGR_3Y,
    CAGR_1Y,
    SHARPE_RATIO
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun FundListScreen(viewModel: FundListViewModel) {
    val state by viewModel.state.collectAsState()
    val isRefreshing = state is FundListState.Loading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Top 5 Small Cap (Direct Growth)") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh, // No painterResource needed
                            contentDescription = "Refresh Funds",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
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
                is FundListState.Loading -> {
                    // Loading state is handled by the pull refresh indicator
                    if (!isRefreshing) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                is FundListState.Error -> {
                    AppLogger.d("FundListScreen", "Error loading funds: ${(state as FundListState.Error).message}")
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (state as FundListState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.refresh() }) {
                            Text("Retry")
                        }
                    }
                }

                is FundListState.Success -> {
                    val funds = (state as FundListState.Success).funds
                    AppLogger.d("FundListScreen", "Loaded ${funds.size} funds ${funds.toList()}")
                    FundList(funds = funds)
                }
            }

            // Pull to refresh indicator
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun FundList(funds: List<FundMetrics>, modifier: Modifier = Modifier) {
    var selectedSortOption by remember { mutableStateOf(SortOption.CAGR_5Y) }

    Column(modifier = modifier.fillMaxSize()) {
        // Sorting toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Sort by: ")
            ScrollableTabRow(
                selectedTabIndex = selectedSortOption.ordinal,
                edgePadding = 0.dp,
                modifier = Modifier.weight(1f)
            ) {
                Tab(
                    selected = selectedSortOption == SortOption.CAGR_5Y,
                    onClick = { selectedSortOption = SortOption.CAGR_5Y },
                    text = { Text("5Y CAGR") }
                )
                Tab(
                    selected = selectedSortOption == SortOption.CAGR_3Y,
                    onClick = { selectedSortOption = SortOption.CAGR_3Y },
                    text = { Text("3Y CAGR") }
                )
                Tab(
                    selected = selectedSortOption == SortOption.CAGR_1Y,
                    onClick = { selectedSortOption = SortOption.CAGR_1Y },
                    text = { Text("1Y CAGR") }
                )
                Tab(
                    selected = selectedSortOption == SortOption.SHARPE_RATIO,
                    onClick = { selectedSortOption = SortOption.SHARPE_RATIO },
                    text = { Text("Sharpe") }
                )
            }
        }

        // Sort funds based on selection
        val sortedFunds = when (selectedSortOption) {
            SortOption.CAGR_5Y -> funds.sortedByDescending { it.cagr5yPct ?: Double.MIN_VALUE }
            SortOption.CAGR_3Y -> funds.sortedByDescending { it.cagr3yPct ?: Double.MIN_VALUE }
            SortOption.CAGR_1Y -> funds.sortedByDescending { it.cagr1yPct ?: Double.MIN_VALUE }
            SortOption.SHARPE_RATIO -> funds.sortedByDescending { it.sharpeRatio ?: Double.MIN_VALUE }
        }

        // Display the sorted funds
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(sortedFunds) { fund ->
                FundCard(fund = fund)
            }
            item {
                Spacer(modifier = Modifier.height(64.dp)) // Adjust height as needed
            }
        }

    }
//    Spacer(modifier = Modifier.height(16.dp)) // Add some space at the bottom
}

@Composable
fun FundCard(fund: FundMetrics) {
    // Determine sharpe ratio category and color
    val sharpeCategory = fund.getSharpeRatioCategory()
    val sharpeRatioColor = when (sharpeCategory) {
        SharpeRatioCategory.EXCELLENT -> Color(0xFF4CAF50)  // Green
        SharpeRatioCategory.GOOD -> Color(0xFFFFC107)       // Amber
        SharpeRatioCategory.POOR -> Color(0xFFF44336)       // Red
        SharpeRatioCategory.UNKNOWN -> Color.Gray           // Gray for unknown
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = fund.fundName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))


            Spacer(modifier = Modifier.height(16.dp))

            // CAGR Metrics section
            Text(
                text = "CAGR",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem(
                    label = "1Y",
                    value = fund.cagr1yPct?.formatPct() ?: "—"
                )
                MetricItem(
                    label = "3Y",
                    value = fund.cagr3yPct?.formatPct() ?: "—"
                )
                MetricItem(
                    label = "5Y",
                    value = fund.cagr5yPct?.formatPct() ?: "—"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Returns section
            Text(
                text = "Returns",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem(
                    label = "3M",
                    value = fund.returns3mPct?.formatPct() ?: "—"
                )
                MetricItem(
                    label = "6M",
                    value = fund.returns6mPct?.formatPct() ?: "—"
                )
                MetricItem(
                    label = "1Y",
                    value = fund.returns1yPct?.formatPct() ?: "—"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Risk metrics section
            Text(
                text = "Risk Metrics",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem(
                    label = "Volatility",
                    value = fund.volatilityPct?.formatPct() ?: "—"
                )
                MetricItem(
                    label = "Sharpe Ratio",
                    value = fund.sharpeRatio.formatSharpeRatio(),
                    valueColor = sharpeRatioColor
                )
                MetricItem(
                    label = "Max Drawdown",
                    value = fund.maxDrawdownPct?.formatPct() ?: "—"
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
//            Text("Recommendation: ${fund.recommendation}",
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
        }
    }
}

