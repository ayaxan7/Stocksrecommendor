package com.ayaan.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ayaan.myapplication.data.model.BenchmarkFundMetrics
import com.ayaan.myapplication.util.formatPct
import com.ayaan.myapplication.util.formatSharpeRatio

@Composable
fun BenchmarkFundCard(
    fund: BenchmarkFundMetrics,
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
            // Fund name and recommendation badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = fund.fundName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                fund.recommendation?.let { recommendation ->
                    val badgeColor = when {
                        recommendation.startsWith("STRONG BUY") -> Color(0xFF4CAF50) // Dark Green
                        recommendation.startsWith("BUY") -> Color(0xFF8BC34A) // Light Green
                        recommendation.startsWith("HOLD") -> Color(0xFFFFC107) // Amber
                        recommendation.startsWith("AVOID") -> Color(0xFFF44336) // Red
                        else -> MaterialTheme.colorScheme.primary
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(badgeColor.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = recommendation.split(":")[0],
                            style = MaterialTheme.typography.labelMedium,
                            color = badgeColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
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
                    label = "3 Month",
                    value = fund.returns3mPct?.formatPct() ?: "—"
                )
                MetricItem(
                    label = "6 Month",
                    value = fund.returns6mPct?.formatPct() ?: "—"
                )
                MetricItem(
                    label = "1 Year",
                    value = fund.returns1yPct?.formatPct() ?: "—"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CAGR section
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
                    label = "1 Year",
                    value = fund.cagr1yPct?.formatPct() ?: "—"
                )
                MetricItem(
                    label = "3 Year",
                    value = fund.cagr3yPct?.formatPct() ?: "—"
                )
                MetricItem(
                    label = "5 Year",
                    value = fund.cagr5yPct?.formatPct() ?: "—"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Risk Metrics section
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
                    value = fund.sharpeRatio.formatSharpeRatio()
                )
                MetricItem(
                    label = "Max Drawdown",
                    value = fund.maxDrawdownPct?.formatPct() ?: "—"
                )
            }
        }
    }
}

@Composable
fun RecommendationGuideCard(
    strongBuy: String,
    buy: String,
    hold: String,
    avoid: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Recommendation Guide",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            RecommendationRow(label = "STRONG BUY", description = strongBuy, color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(4.dp))
            RecommendationRow(label = "BUY", description = buy, color = Color(0xFF8BC34A))
            Spacer(modifier = Modifier.height(4.dp))
            RecommendationRow(label = "HOLD", description = hold, color = Color(0xFFFFC107))
            Spacer(modifier = Modifier.height(4.dp))
            RecommendationRow(label = "AVOID", description = avoid, color = Color(0xFFF44336))
        }
    }
}

@Composable
private fun RecommendationRow(label: String, description: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(color.copy(alpha = 0.2f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
    }
}
