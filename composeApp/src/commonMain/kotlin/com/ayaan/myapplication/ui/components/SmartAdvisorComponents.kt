package com.ayaan.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ayaan.myapplication.data.model.InvestmentStrategy
import com.ayaan.myapplication.data.model.PortfolioAllocation
import com.ayaan.myapplication.data.model.RecommendedFundMetrics
import com.ayaan.myapplication.util.formatPct

@Composable
fun RecommendedFundCard(
    fund: RecommendedFundMetrics,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Fund name and risk score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = fund.fundName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                fund.riskScore?.let { score ->
                    val riskColor = when {
                        score <= 3 -> Color(0xFF4CAF50) // Low risk - Green
                        score <= 7 -> Color(0xFFFFC107) // Medium risk - Amber
                        else -> Color(0xFFF44336) // High risk - Red
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(riskColor.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = score.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = riskColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Recommendation
            fund.recommendation?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Performance metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem(
                    label = "Alpha",
                    value = fund.alphaPct?.formatPct() ?: "—"
                )
                MetricItem(
                    label = "Volatility",
                    value = fund.volatilityPct?.formatPct() ?: "—"
                )
                MetricItem(
                    label = "Sharpe Ratio",
                    value = fund.sharpeRatio?.toString() ?: "—"
                )
            }
        }
    }
}

@Composable
fun InvestmentStrategyCard(
    strategy: InvestmentStrategy,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Investment Strategy",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Primary Recommendation:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = strategy.primaryRecommendation,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Suggested Approach:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = strategy.strategy,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Investment Horizon:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = strategy.horizonNote,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun PortfolioAllocationCard(
    allocation: PortfolioAllocation,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Portfolio Allocation",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Simple allocation visualization
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                // Extract percentages from strings like "20-25%" and handle empty strings
                val smallCapPct = if (allocation.smallCapFunds.isNotBlank()) {
                    allocation.smallCapFunds
                        .replace("%", "")
                        .split("-")
                        .mapNotNull { it.trim().toFloatOrNull() }
                        .takeIf { it.isNotEmpty() }
                        ?.average()
                        ?.toFloat() ?: 0.1f
                } else {
                    0.1f
                }

                val largeCapPct = if (allocation.largeCapFunds.isNotBlank()) {
                    allocation.largeCapFunds
                        .replace("%", "")
                        .split("-")
                        .mapNotNull { it.trim().toFloatOrNull() }
                        .takeIf { it.isNotEmpty() }
                        ?.average()
                        ?.toFloat() ?: 0.1f
                } else {
                    0.1f
                }

                // Ensure we have a minimum value for the remaining percentage
                val remainingPct = (100f - (smallCapPct + largeCapPct)).coerceAtLeast(0.1f)

                // Ensure all weights are at least 0.1f to prevent the "invalid weight" error
                Box(
                    modifier = Modifier
                        .weight(smallCapPct.coerceAtLeast(0.1f))
                        .fillMaxHeight()
                        .background(Color(0xFF4CAF50)) // Small Cap - Green
                )
                Box(
                    modifier = Modifier
                        .weight(largeCapPct.coerceAtLeast(0.1f))
                        .fillMaxHeight()
                        .background(Color(0xFF2196F3)) // Large Cap - Blue
                )
                Box(
                    modifier = Modifier
                        .weight(remainingPct)
                        .fillMaxHeight()
                        .background(Color(0xFFBDBDBD)) // Other - Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                AllocationLegendItem(
                    label = "Small Cap",
                    value = allocation.smallCapFunds.ifBlank { "0%" },
                    color = Color(0xFF4CAF50)
                )
                AllocationLegendItem(
                    label = "Large Cap",
                    value = allocation.largeCapFunds.ifBlank { "0%" },
                    color = Color(0xFF2196F3)
                )
                AllocationLegendItem(
                    label = "Other",
                    value = "Remaining",
                    color = Color(0xFFBDBDBD)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = allocation.recommendation,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (allocation.topFundSuggestion.isNotBlank()) {
                Text(
                    text = "Top Fund Suggestion:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = allocation.topFundSuggestion,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun AllocationLegendItem(
    label: String,
    value: String,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun RiskWarningsCard(
    warnings: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Risk Warnings",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))

            warnings.forEach { warning ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "• ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = warning,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}
