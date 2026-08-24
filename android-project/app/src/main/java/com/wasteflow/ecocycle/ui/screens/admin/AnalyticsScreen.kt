package com.wasteflow.ecocycle.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wasteflow.ecocycle.ui.components.BrutalistButton
import com.wasteflow.ecocycle.ui.components.BrutalistCard
import com.wasteflow.ecocycle.ui.components.BrutalistShape
import com.wasteflow.ecocycle.ui.theme.*

@Composable
fun AnalyticsScreen(
    onNavigateBack: () -> Unit
) {
    var timeframe by remember { mutableStateOf("MONTHLY") }
    var isExported by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoSurface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = EcoOnSurface)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "ADMIN DASHBOARD",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                color = EcoOnSurface
            )
        }

        // Toggle WEEKLY vs MONTHLY
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(EcoSurfaceContainerHigh)
                .border(2.dp, EcoOnSurface)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(if (timeframe == "WEEKLY") EcoPrimary else Color.Transparent)
                    .clickable { timeframe = "WEEKLY" },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "WEEKLY",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (timeframe == "WEEKLY") EcoWhite else EcoOnSurface
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(if (timeframe == "MONTHLY") EcoPrimary else Color.Transparent)
                    .clickable { timeframe = "MONTHLY" },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "MONTHLY",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (timeframe == "MONTHLY") EcoWhite else EcoOnSurface
                )
            }
        }

        // Stat Numbers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BrutalistCard(
                modifier = Modifier.weight(1f),
                backgroundColor = EcoSurfaceContainerHighest,
                shadowOffset = 4.dp
            ) {
                Text("TOTAL COLLECTED", style = MaterialTheme.typography.labelSmall, color = EcoOnSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "14.2T",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                    color = EcoOnSurface
                )
            }

            BrutalistCard(
                modifier = Modifier.weight(1f),
                backgroundColor = EcoPrimary,
                shadowOffset = 4.dp
            ) {
                Text("GROWTH", style = MaterialTheme.typography.labelSmall, color = EcoWhite)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "+12%",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                    color = EcoWhite
                )
            }
        }

        // Waste Distribution
        BrutalistCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = EcoSurfaceContainerLow,
            shadowOffset = 2.dp
        ) {
            Text(
                text = "WASTE DISTRIBUTION",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                color = EcoOnSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = EcoOnSurface, thickness = 2.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Brutalist 5-Bar Graphic
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(EcoSurface)
                    .border(2.dp, EcoOnSurface)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Box(modifier = Modifier.weight(1f).fillMaxHeight(0.85f).background(EcoPrimary).border(1.dp, EcoOnSurface))
                Box(modifier = Modifier.weight(1f).fillMaxHeight(0.60f).background(EcoPrimaryFixedDim).border(1.dp, EcoOnSurface))
                Box(modifier = Modifier.weight(1f).fillMaxHeight(0.40f).background(EcoPrimaryFixed).border(1.dp, EcoOnSurface))
                Box(modifier = Modifier.weight(1f).fillMaxHeight(0.70f).background(EcoPrimaryContainer).border(1.dp, EcoOnSurface))
                Box(modifier = Modifier.weight(1f).fillMaxHeight(0.25f).background(EcoTertiaryContainer).border(1.dp, EcoOnSurface))
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("PLA", style = MaterialTheme.typography.labelSmall)
                Text("PAP", style = MaterialTheme.typography.labelSmall)
                Text("MET", style = MaterialTheme.typography.labelSmall)
                Text("ORG", style = MaterialTheme.typography.labelSmall)
                Text("OTH", style = MaterialTheme.typography.labelSmall)
            }
        }

        // Target Progress (Donut gauge simulator)
        BrutalistCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = EcoSurfaceContainerLow,
            shadowOffset = 2.dp
        ) {
            Text(
                text = "TARGET PROGRESS",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                color = EcoOnSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = EcoOnSurface, thickness = 2.dp)
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(EcoPrimary, BrutalistShape)
                        .border(3.dp, EcoOnSurface, BrutalistShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "75%",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = EcoWhite
                        )
                    )
                }
            }
        }

        // Export Data Button
        BrutalistButton(
            text = if (isExported) "DATA EXPORTED (CSV)" else "EXPORT DATA",
            onClick = { isExported = true },
            backgroundColor = EcoPrimary,
            contentColor = EcoWhite,
            icon = {
                Icon(Icons.Default.Download, contentDescription = null, tint = EcoWhite)
            }
        )
    }
}
