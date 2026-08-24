package com.wasteflow.ecocycle.ui.screens.citizen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.wasteflow.ecocycle.data.model.User
import com.wasteflow.ecocycle.ui.components.BrutalistBadge
import com.wasteflow.ecocycle.ui.components.BrutalistCard
import com.wasteflow.ecocycle.ui.components.BrutalistShape
import com.wasteflow.ecocycle.ui.theme.*

@Composable
fun CitizenDashboardScreen(
    user: User,
    onNavigateLiveRoute: () -> Unit,
    onNavigateReportIssue: () -> Unit,
    onNavigateRedeemPoints: () -> Unit,
    onNavigateElectricityBill: () -> Unit,
    onRoleSwitchClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoSurfaceContainerHigh)
            .statusBarsPadding()
    ) {
        // App Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(EcoSurface)
                .border(width = 1.dp, color = EcoOnSurface.copy(alpha = 0.1f))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DeleteSweep,
                    contentDescription = null,
                    tint = EcoPrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "WASTEFLOW",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    ),
                    color = EcoOnSurface
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(EcoPrimaryContainer)
                        .clickable { onRoleSwitchClick() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "USER",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = EcoOnPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(EcoPrimary)
                        .clickable { onRoleSwitchClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = EcoWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Scrollable Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Hey Alex + Balance
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(EcoSurface)
                            .border(2.dp, EcoOnSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Eco,
                            contentDescription = null,
                            tint = EcoPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "HEY, ",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                        color = EcoOnSurface
                    )
                    Text(
                        text = user.name.uppercase(),
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                        color = EcoPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Balance Box
                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = EcoPrimaryContainer,
                    borderColor = EcoOnSurface,
                    shadowOffset = 4.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "YOUR BALANCE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    letterSpacing = 1.sp
                                ),
                                color = EcoOnPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "%,d".format(user.balancePoints),
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 42.sp
                                ),
                                color = EcoOnPrimaryContainer
                            )
                        }
                        Text(
                            text = "PTS",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = EcoOnPrimaryContainer
                        )
                    }
                }
            }

            // Live Route Section
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(20.dp)
                            .background(EcoOnSurface)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LIVE ROUTE",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = EcoOnSurface
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = EcoSurface,
                    shadowOffset = 4.dp,
                    onClick = onNavigateLiveRoute
                ) {
                    // Card Top bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(EcoPrimaryFixedDim)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = EcoOnSurface)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "TRUCK #402",
                                style = MaterialTheme.typography.labelLarge,
                                color = EcoOnSurface
                            )
                        }
                        BrutalistBadge(
                            text = "EN ROUTE",
                            backgroundColor = EcoOnSurface,
                            contentColor = EcoSurface
                        )
                    }

                    // Map Simulator Graphic
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .background(EcoSurfaceContainer)
                            .border(1.dp, EcoOnSurface.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Navigation,
                                contentDescription = null,
                                tint = EcoPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "Downtown / Zone B Corridor",
                                style = MaterialTheme.typography.labelSmall,
                                color = EcoOnSurfaceVariant
                            )
                        }
                    }

                    // ETA Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ETA: 14 mins",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = EcoOnSurface
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Open",
                            tint = EcoOnSurface
                        )
                    }
                }
            }

            // Stats & Schedule Grid (2 Columns)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Impact Card
                BrutalistCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = EcoTertiary,
                    shadowOffset = 4.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "IMPACT",
                            style = MaterialTheme.typography.labelSmall,
                            color = EcoWhite
                        )
                        Icon(Icons.Default.Eco, contentDescription = null, tint = EcoWhite, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "${user.recycledKgYtd.toInt()}kg",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                        color = EcoWhite
                    )
                    Text(
                        text = "RECYCLED YTD",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = EcoTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    // Mini Brutalist bar chart
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(modifier = Modifier.weight(1f).fillMaxHeight(0.4f).background(EcoWhite).border(1.dp, EcoOnSurface))
                        Box(modifier = Modifier.weight(1f).fillMaxHeight(0.6f).background(EcoWhite).border(1.dp, EcoOnSurface))
                        Box(modifier = Modifier.weight(1f).fillMaxHeight(0.3f).background(EcoWhite).border(1.dp, EcoOnSurface))
                        Box(modifier = Modifier.weight(1f).fillMaxHeight(0.9f).background(EcoPrimaryContainer).border(1.dp, EcoOnSurface))
                    }
                }

                // Next Pickup Card
                BrutalistCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = EcoSurface,
                    shadowOffset = 4.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "NEXT PICKUP",
                            style = MaterialTheme.typography.labelSmall,
                            color = EcoOnSurface
                        )
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = EcoOnSurface, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "TUE",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                        color = EcoOnSurface
                    )
                    Text(
                        text = "14 NOV",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = EcoOnSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        BrutalistBadge(text = "PLASTIC", backgroundColor = EcoOnSurface, contentColor = EcoSurface)
                        BrutalistBadge(text = "GLASS", backgroundColor = EcoOnSurface, contentColor = EcoSurface)
                    }
                }
            }

            // Actions Section
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(20.dp)
                            .background(EcoOnSurface)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ACTIONS",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = EcoOnSurface
                    )
                }

                // Report Issue
                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = EcoSurface,
                    shadowOffset = 4.dp,
                    onClick = onNavigateReportIssue
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(EcoError)
                                    .border(2.dp, EcoOnSurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Report, contentDescription = null, tint = EcoWhite)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "REPORT ISSUE",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = EcoOnSurface
                            )
                        }
                        Icon(Icons.Default.ArrowOutward, contentDescription = null, tint = EcoOnSurface)
                    }
                }

                // Pay Electricity Bill
                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = EcoSurface,
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
                                    .size(38.dp)
                                    .background(EcoPrimaryFixed)
                                    .border(2.dp, EcoOnSurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = EcoPrimary)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "ELECTRICITY BILL PAYMENT",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = EcoOnSurface
                                )
                                Text(
                                    text = "Redeem points for power bill credit",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = EcoOnSurfaceVariant
                                )
                            }
                        }
                        Icon(Icons.Default.ArrowOutward, contentDescription = null, tint = EcoOnSurface)
                    }
                }

                // Points Wallet & History
                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = EcoSurface,
                    shadowOffset = 4.dp,
                    onClick = onNavigateRedeemPoints
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(EcoPrimaryContainer)
                                    .border(2.dp, EcoOnSurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = EcoOnPrimaryContainer)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "REWARD POINTS WALLET",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = EcoOnSurface
                                )
                                Text(
                                    text = "View balance & transaction ledger",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = EcoOnSurfaceVariant
                                )
                            }
                        }
                        Icon(Icons.Default.ArrowOutward, contentDescription = null, tint = EcoOnSurface)
                    }
                }
            }
        }
    }
}
