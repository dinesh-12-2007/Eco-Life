package com.wasteflow.ecocycle.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wasteflow.ecocycle.ui.components.BrutalistBadge
import com.wasteflow.ecocycle.ui.components.BrutalistCard
import com.wasteflow.ecocycle.ui.theme.*

@Composable
fun HeatmapScreen(
    onNavigateBack: () -> Unit,
    onViewSectorDetails: () -> Unit
) {
    var activeTab by remember { mutableStateOf("COMPLAINTS") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoSurfaceContainerLow)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(EcoSurface)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = EcoOnSurface)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "ADMIN DASHBOARD",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                color = EcoOnSurface
            )
        }

        // Map Visual Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(EcoSurfaceContainerHigh)
                .border(width = 2.dp, color = EcoOnSurface)
        ) {
            // Simulated Heatmap Grid
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = EcoTertiary
                )
                Text(
                    text = "METROPOLITAN INCIDENTS HEATMAP",
                    style = MaterialTheme.typography.labelSmall,
                    color = EcoOnSurfaceVariant
                )
            }

            // Top Toggle Buttons
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(if (activeTab == "COMPLAINTS") EcoPrimaryContainer else EcoSurface)
                        .border(2.dp, EcoOnSurface)
                        .clickable { activeTab = "COMPLAINTS" }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "COMPLAINTS",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (activeTab == "COMPLAINTS") EcoOnPrimaryContainer else EcoOnSurface
                    )
                }
                Box(
                    modifier = Modifier
                        .background(if (activeTab == "COLLECTIONS") EcoPrimaryContainer else EcoSurface)
                        .border(2.dp, EcoOnSurface)
                        .clickable { activeTab = "COLLECTIONS" }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "COLLECTIONS",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (activeTab == "COLLECTIONS") EcoOnPrimaryContainer else EcoOnSurface
                    )
                }
            }

            // Legend Overlay (Bottom Right)
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.BottomEnd)
                    .background(EcoSurface)
                    .border(2.dp, EcoOnSurface)
                    .padding(8.dp)
            ) {
                Column {
                    Text(
                        text = "DENSITY",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                        color = EcoOnSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(10.dp).background(EcoError).border(1.dp, EcoOnSurface))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("HIGH", style = MaterialTheme.typography.labelSmall)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(10.dp).background(EcoTertiaryContainer).border(1.dp, EcoOnSurface))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("MED", style = MaterialTheme.typography.labelSmall)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(10.dp).background(EcoPrimaryContainer).border(1.dp, EcoOnSurface))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("LOW", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        // Details Body
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Hotspot Card
            BrutalistCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = EcoSurface,
                shadowOffset = 4.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "TOP HOTSPOT",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                            color = EcoTertiary
                        )
                        Text(
                            text = "SECTOR 7",
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                            color = EcoOnSurface
                        )
                    }
                    BrutalistBadge(
                        text = "ALERT",
                        backgroundColor = EcoError,
                        contentColor = EcoWhite
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = EcoOnSurface, thickness = 2.dp)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "42",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = EcoError
                            )
                        )
                        Text("INCIDENTS", style = MaterialTheme.typography.labelSmall, color = EcoTertiary)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onViewSectorDetails() }
                    ) {
                        Text(
                            text = "VIEW DETAILS",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = EcoOnSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = EcoOnSurface)
                    }
                }
            }

            // Bento Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BrutalistCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = EcoPrimaryContainer,
                    shadowOffset = 4.dp
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = EcoOnPrimaryContainer, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "+15%",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                        color = EcoOnPrimaryContainer
                    )
                    Text("VS LAST WK", style = MaterialTheme.typography.labelSmall, color = EcoOnPrimaryContainer)
                }

                BrutalistCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = EcoSurface,
                    shadowOffset = 4.dp
                ) {
                    Icon(Icons.Default.LocalShipping, contentDescription = null, tint = EcoOnSurface, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "12",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                        color = EcoOnSurface
                    )
                    Text("ACTIVE UNITS", style = MaterialTheme.typography.labelSmall, color = EcoOnSurfaceVariant)
                }
            }
        }
    }
}
