package com.wasteflow.ecocycle.ui.screens.citizen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.wasteflow.ecocycle.ui.components.BrutalistBadge
import com.wasteflow.ecocycle.ui.components.BrutalistButton
import com.wasteflow.ecocycle.ui.components.BrutalistCard
import com.wasteflow.ecocycle.ui.theme.*

@Composable
fun LiveTrackingScreen(
    onNavigateBack: () -> Unit,
    onReportIssue: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoSurface)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Simulated Map View (Full screen canvas)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(EcoSurfaceContainer)
        ) {
            // Simulated Roads Grid / Map visual
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp, bottom = 220.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animated Truck Marker on Map
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(EcoPrimaryContainer)
                        .border(3.dp, EcoOnSurface, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = "Truck Location",
                        tint = EcoOnPrimaryContainer,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .background(EcoOnSurface)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "TRUCK #402 (LIVE GPS)",
                        style = MaterialTheme.typography.labelSmall,
                        color = EcoSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Downtown Los Angeles -> Zone B Sector",
                    style = MaterialTheme.typography.bodySmall,
                    color = EcoOnSurfaceVariant
                )
            }
        }

        // Top Status Header Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter)
        ) {
            // Back row
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .background(EcoSurface, CircleShape)
                    .border(2.dp, EcoOnSurface, CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = EcoOnSurface)
            }

            Spacer(modifier = Modifier.height(12.dp))

            BrutalistCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = EcoSurface,
                shadowOffset = 4.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TRUCK #402",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                            color = EcoOnSurface
                        )
                        Text(
                            text = "LIVE TRACKING",
                            style = MaterialTheme.typography.labelSmall,
                            color = EcoOnSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(EcoPrimary)
                            .border(2.dp, EcoOnSurface)
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "12",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 20.sp
                                ),
                                color = EcoWhite
                            )
                            Text(
                                text = "MIN ETA",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                color = EcoWhite
                            )
                        }
                    }
                }
            }
        }

        // Bottom Driver Information Sheet
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(EcoSurface)
                .border(2.dp, EcoOnSurface)
                .padding(20.dp)
        ) {
            // Drag handle
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(48.dp)
                    .height(4.dp)
                    .background(EcoOnSurface)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Driver Avatar
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(EcoSurfaceContainerHighest)
                        .border(2.dp, EcoOnSurface, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = EcoOnSurface
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Sarah Jenkins",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = EcoOnSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        BrutalistBadge(
                            text = "EN ROUTE",
                            backgroundColor = EcoPrimary,
                            contentColor = EcoWhite
                        )
                        BrutalistBadge(
                            text = "ZONE B",
                            backgroundColor = EcoSurfaceContainerHighest,
                            contentColor = EcoOnSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Issue Button
                Box(modifier = Modifier.weight(1f)) {
                    BrutalistButton(
                        text = "ISSUE",
                        onClick = onReportIssue,
                        backgroundColor = EcoSurfaceContainerHigh,
                        contentColor = EcoOnSurface,
                        icon = {
                            Icon(Icons.Default.Report, contentDescription = null, tint = EcoOnSurface)
                        }
                    )
                }

                // Contact Button
                Box(modifier = Modifier.weight(2f)) {
                    BrutalistButton(
                        text = "CONTACT",
                        onClick = { /* Simulated dialer */ },
                        backgroundColor = EcoPrimary,
                        contentColor = EcoWhite,
                        icon = {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = EcoWhite)
                        }
                    )
                }
            }
        }
    }
}
