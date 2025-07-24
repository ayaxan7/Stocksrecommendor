package com.ayaan.myapplication.ui.smartadvisor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ayaan.myapplication.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartAdvisorScreen(
    viewModel: SmartAdvisorViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val riskTolerance by viewModel.riskTolerance.collectAsState()
    val investmentHorizon by viewModel.investmentHorizon.collectAsState()
    val minAlpha by viewModel.minAlpha.collectAsState()
    val maxVolatility by viewModel.maxVolatility.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smart Advisor") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                },
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
        ) {
            when (state) {
                is SmartAdvisorState.Form -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Risk Tolerance Section
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "What is your risk tolerance?",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Risk tolerance options
                                    Column (
                                        modifier = Modifier.fillMaxHeight(),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        RiskToleranceOption(
                                            title = "Conservative",
                                            description = "Lower risk, stable returns",
                                            isSelected = riskTolerance == "conservative",
                                            onClick = { viewModel.updateRiskTolerance("conservative") }
                                        )

                                        RiskToleranceOption(
                                            title = "Moderate",
                                            description = "Balanced risk/reward",
                                            isSelected = riskTolerance == "moderate",
                                            onClick = { viewModel.updateRiskTolerance("moderate") }
                                        )

                                        RiskToleranceOption(
                                            title = "Aggressive",
                                            description = "Higher risk, higher potential returns",
                                            isSelected = riskTolerance == "aggressive",
                                            onClick = { viewModel.updateRiskTolerance("aggressive") }
                                        )
                                    }
                                }
                            }
                        }

                        // Investment Horizon Section
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "What is your investment horizon?",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Investment horizon options
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        InvestmentHorizonOption(
                                            title = "Short Term",
                                            description = "1-3 years",
                                            isSelected = investmentHorizon == "short_term",
                                            onClick = { viewModel.updateInvestmentHorizon("short_term") }
                                        )

                                        InvestmentHorizonOption(
                                            title = "Medium Term",
                                            description = "3-7 years",
                                            isSelected = investmentHorizon == "medium_term",
                                            onClick = { viewModel.updateInvestmentHorizon("medium_term") }
                                        )

                                        InvestmentHorizonOption(
                                            title = "Long Term",
                                            description = "7+ years",
                                            isSelected = investmentHorizon == "long_term",
                                            onClick = { viewModel.updateInvestmentHorizon("long_term") }
                                        )
                                    }
                                }
                            }
                        }

                        // Alpha and Volatility Filters
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Advanced Filters (Optional)",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Min Alpha slider
                                    Text(
                                        text = "Minimum Alpha: ${minAlpha?.toString() ?: "0.0"}%",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Slider(
                                        value = minAlpha?.toFloat() ?: 0f,
                                        onValueChange = { viewModel.updateMinAlpha(it.toDouble()) },
                                        valueRange = 0f..5f,
                                        steps = 10
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Max Volatility slider
                                    Text(
                                        text = "Maximum Volatility: ${maxVolatility?.toString() ?: "25.0"}%",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Slider(
                                        value = maxVolatility?.toFloat() ?: 25f,
                                        onValueChange = { viewModel.updateMaxVolatility(it.toDouble()) },
                                        valueRange = 10f..40f,
                                        steps = 15
                                    )
                                }
                            }
                        }

                        // Get Recommendations Button
                        item {
                            Button(
                                onClick = { viewModel.getRecommendations() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Get Recommendations")
                            }
                        }
                    }
                }

                is SmartAdvisorState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                is SmartAdvisorState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = (state as SmartAdvisorState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = { viewModel.resetToForm() }) {
                            Text("Go Back")
                        }
                    }
                }

                is SmartAdvisorState.Success -> {
                    val advisorData = (state as SmartAdvisorState.Success).advisorData

                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Summary Card
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Analysis Summary",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    with(advisorData.analysisSummary) {
                                        Text("Total Funds Analyzed: $totalFundsAnalyzed")
                                        Text("Funds with Positive Alpha: $fundsWithPositiveAlpha")
                                        Text("Average Alpha: ${averageAlphaPct?.toString() ?: "N/A"}%")
                                        Text("Average Volatility: ${averageVolatilityPct?.toString() ?: "N/A"}%")
                                        Text("Market Performance: $marketOutperformance")
                                    }
                                }
                            }
                        }

                        // Recommended Funds
                        item {
                            Text(
                                text = "Recommended Funds",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }

                        items(advisorData.recommendedFunds) { fund ->
                            RecommendedFundCard(fund = fund)
                        }

                        // Investment Strategy
                        item {
                            InvestmentStrategyCard(strategy = advisorData.investmentStrategy)
                        }

                        // Portfolio Allocation
                        item {
                            PortfolioAllocationCard(allocation = advisorData.portfolioAllocation)
                        }

                        // Market Outlook
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Market Outlook",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = advisorData.marketOutlook,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        // Risk Warnings
                        item {
                            RiskWarningsCard(warnings = advisorData.riskWarnings)
                        }

                        // Go Back Button
                        item {
                            Button(
                                onClick = { viewModel.resetToForm() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Reset Parameters")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RiskToleranceOption(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}

@Composable
fun InvestmentHorizonOption(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}
