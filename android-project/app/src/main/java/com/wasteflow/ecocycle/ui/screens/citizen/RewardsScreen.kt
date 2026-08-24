package com.wasteflow.ecocycle.ui.screens.citizen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wasteflow.ecocycle.data.model.*
import com.wasteflow.ecocycle.ui.components.BrutalistBadge
import com.wasteflow.ecocycle.ui.components.BrutalistButton
import com.wasteflow.ecocycle.ui.components.BrutalistCard
import com.wasteflow.ecocycle.ui.theme.*

@Composable
fun RewardsScreen(
    user: User,
    transactions: List<RewardTransaction>,
    conversionConfig: PointsConversionConfig,
    onNavigateElectricityBill: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf<TransactionType?>(null) }

    val totalEarned = remember(transactions) {
        transactions.filter { it.type == TransactionType.EARN }.sumOf { it.points }
    }
    val totalUsed = remember(transactions) {
        transactions.filter { it.type == TransactionType.REDEEM }.sumOf { it.points }
    }

    val filteredTransactions = remember(transactions, selectedFilter) {
        if (selectedFilter == null) transactions else transactions.filter { it.type == selectedFilter }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoSurface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = EcoOnSurface)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "REWARD POINTS WALLET",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                    color = EcoOnSurface
                )
                Text(
                    text = "Track points earned from recycling & bill discounts",
                    style = MaterialTheme.typography.bodySmall,
                    color = EcoOnSurfaceVariant
                )
            }
        }

        // Current Balance Banner
        BrutalistCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = EcoPrimaryContainer,
            shadowOffset = 4.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ACTIVE WALLET BALANCE",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = EcoOnPrimaryContainer
                    )
                    Text(
                        text = "${user.balancePoints} PTS",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                        color = EcoOnPrimaryContainer
                    )
                    Text(
                        text = "≈ ${conversionConfig.currencySymbol}${String.format("%.2f", user.balancePoints.toDouble() / conversionConfig.pointsPerUnit)} Electricity Credit",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = EcoOnPrimaryContainer
                    )
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(EcoOnPrimaryContainer)
                        .border(2.dp, EcoOnSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.ElectricBolt,
                        contentDescription = null,
                        tint = EcoPrimaryContainer,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Total Earned vs Total Used Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Earned Box
            BrutalistCard(
                modifier = Modifier.weight(1f),
                backgroundColor = EcoSurfaceContainerLowest,
                shadowOffset = 2.dp
            ) {
                Text(
                    text = "TOTAL EARNED",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = EcoOnSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "+$totalEarned PTS",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                    color = EcoPrimary
                )
            }

            // Used Box
            BrutalistCard(
                modifier = Modifier.weight(1f),
                backgroundColor = EcoSurfaceContainerLowest,
                shadowOffset = 2.dp
            ) {
                Text(
                    text = "TOTAL REDEEMED",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = EcoOnSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "-$totalUsed PTS",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                    color = EcoOnSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Primary Action: Pay Electricity Bill
        BrutalistCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = EcoPrimaryFixed,
            shadowOffset = 4.dp,
            onClick = onNavigateElectricityBill
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(EcoPrimary)
                            .border(2.dp, EcoOnSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ElectricMeter, contentDescription = null, tint = EcoWhite)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "PAY ELECTRICITY BILL",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black),
                            color = EcoOnSurface
                        )
                        Text(
                            text = "Convert ${conversionConfig.description}",
                            style = MaterialTheme.typography.bodySmall,
                            color = EcoOnSurface
                        )
                    }
                }
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = EcoOnSurface)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ledger / History Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TRANSACTION LEDGER",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                color = EcoOnSurface
            )

            // Filter Chips
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    modifier = Modifier
                        .background(if (selectedFilter == null) EcoOnSurface else EcoSurfaceContainer)
                        .border(1.dp, EcoOnSurface)
                        .clickable { selectedFilter = null }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ALL",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (selectedFilter == null) EcoWhite else EcoOnSurface
                    )
                }
                Box(
                    modifier = Modifier
                        .background(if (selectedFilter == TransactionType.EARN) EcoOnSurface else EcoSurfaceContainer)
                        .border(1.dp, EcoOnSurface)
                        .clickable { selectedFilter = TransactionType.EARN }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "EARNED",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (selectedFilter == TransactionType.EARN) EcoWhite else EcoOnSurface
                    )
                }
                Box(
                    modifier = Modifier
                        .background(if (selectedFilter == TransactionType.REDEEM) EcoOnSurface else EcoSurfaceContainer)
                        .border(1.dp, EcoOnSurface)
                        .clickable { selectedFilter = TransactionType.REDEEM }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "REDEEMED",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (selectedFilter == TransactionType.REDEEM) EcoWhite else EcoOnSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Transactions List
        if (filteredTransactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No transactions found.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = EcoOnSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTransactions) { tx ->
                    val isEarn = tx.type == TransactionType.EARN
                    BrutalistCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = EcoSurfaceContainerLowest,
                        shadowOffset = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(if (isEarn) EcoPrimaryContainer else EcoSecondaryContainer)
                                        .border(1.5.dp, EcoOnSurface),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        if (isEarn) Icons.Default.Add else Icons.Default.Remove,
                                        contentDescription = null,
                                        tint = if (isEarn) EcoOnPrimaryContainer else EcoOnSurface,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = tx.description,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = EcoOnSurface
                                    )
                                    Text(
                                        text = "${tx.date} • Ref: ${tx.referenceId ?: tx.id}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = EcoOnSurfaceVariant
                                    )
                                }
                            }
                            Text(
                                text = if (isEarn) "+${tx.points} PTS" else "-${tx.points} PTS",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                color = if (isEarn) EcoPrimary else EcoOnSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
