package com.wasteflow.ecocycle.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.wasteflow.ecocycle.data.model.ServiceTask
import com.wasteflow.ecocycle.data.model.TaskStatus
import com.wasteflow.ecocycle.data.model.TaskType
import com.wasteflow.ecocycle.ui.components.BrutalistBadge
import com.wasteflow.ecocycle.ui.components.BrutalistButton
import com.wasteflow.ecocycle.ui.components.BrutalistCard
import com.wasteflow.ecocycle.ui.theme.*

@Composable
fun EmployeeDashboardScreen(
    tasks: List<ServiceTask>,
    onAcceptTask: (String) -> Unit,
    onStartTask: (String) -> Unit,
    onCompleteTask: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateLogWaste: () -> Unit,
    onNavigateHeatmap: () -> Unit,
    onNavigateAnalytics: () -> Unit,
    onNavigateRoster: () -> Unit,
    onNavigateComplaints: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoSurface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        // Top Header with Back & Menu
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = EcoOnSurface)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ADMIN DASHBOARD",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    ),
                    color = EcoOnSurface
                )
            }

            IconButton(onClick = onNavigateLogWaste) {
                Icon(Icons.Default.AddBox, contentDescription = "Log Waste", tint = EcoPrimary)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Navigation Bar for Admin Sections
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Button(
                onClick = onNavigateLogWaste,
                modifier = Modifier.weight(1f).border(1.dp, EcoOnSurface),
                colors = ButtonDefaults.buttonColors(containerColor = EcoPrimaryContainer, contentColor = EcoOnPrimaryContainer),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
            ) {
                Text("LOG", style = MaterialTheme.typography.labelSmall)
            }
            Button(
                onClick = onNavigateHeatmap,
                modifier = Modifier.weight(1f).border(1.dp, EcoOnSurface),
                colors = ButtonDefaults.buttonColors(containerColor = EcoSurfaceContainerHighest, contentColor = EcoOnSurface),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
            ) {
                Text("MAP", style = MaterialTheme.typography.labelSmall)
            }
            Button(
                onClick = onNavigateAnalytics,
                modifier = Modifier.weight(1f).border(1.dp, EcoOnSurface),
                colors = ButtonDefaults.buttonColors(containerColor = EcoSurfaceContainerHighest, contentColor = EcoOnSurface),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
            ) {
                Text("STATS", style = MaterialTheme.typography.labelSmall)
            }
            Button(
                onClick = onNavigateRoster,
                modifier = Modifier.weight(1f).border(1.dp, EcoOnSurface),
                colors = ButtonDefaults.buttonColors(containerColor = EcoSurfaceContainerHighest, contentColor = EcoOnSurface),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
            ) {
                Text("CREW", style = MaterialTheme.typography.labelSmall)
            }
            Button(
                onClick = onNavigateComplaints,
                modifier = Modifier.weight(1f).border(1.dp, EcoOnSurface),
                colors = ButtonDefaults.buttonColors(containerColor = EcoSurfaceContainerHighest, contentColor = EcoOnSurface),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
            ) {
                Text("ALERTS", style = MaterialTheme.typography.labelSmall)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Shift Impact Banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 0.dp, color = Color.Transparent)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = "SHIFT",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                    color = EcoOnSurfaceVariant
                )
                Text(
                    text = "IMPACT",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                    color = EcoOnSurface
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${tasks.size}",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = EcoPrimary
                    )
                )
                Text(
                    text = "ACTIVE TASKS",
                    style = MaterialTheme.typography.labelSmall,
                    color = EcoOnSurfaceVariant
                )
            }
        }

        Divider(color = EcoOnSurface, thickness = 2.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Task Cards List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(tasks) { task ->
                val isRoutine = task.type == TaskType.ROUTINE
                val cardBg = if (isRoutine) EcoPrimary else EcoSurfaceContainerLowest

                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = cardBg,
                    shadowOffset = 4.dp
                ) {
                    // Header Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            BrutalistBadge(
                                text = task.type.name.replace("_", " "),
                                backgroundColor = EcoOnSurface,
                                contentColor = EcoSurface
                            )
                            if (task.status == TaskStatus.ASSIGNED) {
                                BrutalistBadge(
                                    text = "ASSIGNED",
                                    backgroundColor = EcoError,
                                    contentColor = EcoWhite
                                )
                            } else if (task.status == TaskStatus.IN_PROGRESS) {
                                BrutalistBadge(
                                    text = "IN PROGRESS",
                                    backgroundColor = EcoSurface,
                                    contentColor = EcoOnSurface
                                )
                            } else {
                                BrutalistBadge(
                                    text = task.status.name,
                                    backgroundColor = EcoSurfaceContainerHighest,
                                    contentColor = EcoOnSurface
                                )
                            }
                        }

                        if (task.priority == "URGENT") {
                            BrutalistBadge(
                                text = "URGENT",
                                backgroundColor = EcoSurfaceContainerHighest,
                                contentColor = EcoOnSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = task.title.uppercase(),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = if (isRoutine) EcoWhite else EcoOnSurface
                    )
                    Text(
                        text = task.location,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isRoutine) EcoWhite.copy(alpha = 0.8f) else EcoOnSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            tint = if (isRoutine) EcoWhite else EcoOnSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = task.time,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isRoutine) EcoWhite else EcoOnSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = if (isRoutine) EcoWhite else EcoOnSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = task.distance,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isRoutine) EcoWhite else EcoOnSurfaceVariant
                        )
                    }

                    // Striped Route Progress for Routine
                    if (isRoutine) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("ROUTE PROGRESS", style = MaterialTheme.typography.labelSmall, color = EcoWhite)
                            Text("${task.progress}%", style = MaterialTheme.typography.labelSmall, color = EcoWhite)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .background(EcoSurface)
                                .border(2.dp, EcoOnSurface)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(task.progress / 100f)
                                    .fillMaxHeight()
                                    .background(EcoOnSurface)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = if (isRoutine) EcoWhite.copy(alpha = 0.4f) else EcoOnSurface, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Task Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        when (task.status) {
                            TaskStatus.ASSIGNED -> {
                                Box(modifier = Modifier.weight(1f)) {
                                    BrutalistButton(
                                        text = "ACCEPT",
                                        onClick = { onAcceptTask(task.id) },
                                        backgroundColor = EcoPrimary,
                                        contentColor = EcoWhite
                                    )
                                }
                                Box(modifier = Modifier.size(48.dp)) {
                                    BrutalistButton(
                                        text = "",
                                        onClick = { /* Navigation */ },
                                        backgroundColor = EcoSurfaceContainerHigh,
                                        contentColor = EcoOnSurface,
                                        icon = { Icon(Icons.Default.Navigation, contentDescription = null, tint = EcoOnSurface) }
                                    )
                                }
                            }
                            TaskStatus.PENDING -> {
                                Box(modifier = Modifier.weight(1f)) {
                                    BrutalistButton(
                                        text = "START TASK",
                                        onClick = { onStartTask(task.id) },
                                        backgroundColor = EcoSurfaceContainerHigh,
                                        contentColor = EcoOnSurface
                                    )
                                }
                                Box(modifier = Modifier.size(48.dp)) {
                                    BrutalistButton(
                                        text = "",
                                        onClick = { /* Navigation */ },
                                        backgroundColor = EcoSurfaceContainerHigh,
                                        contentColor = EcoOnSurface,
                                        icon = { Icon(Icons.Default.Navigation, contentDescription = null, tint = EcoOnSurface) }
                                    )
                                }
                            }
                            TaskStatus.IN_PROGRESS -> {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    BrutalistButton(
                                        text = "COMPLETE",
                                        onClick = { onCompleteTask(task.id) },
                                        backgroundColor = EcoSurface,
                                        contentColor = EcoOnSurface,
                                        icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EcoPrimary) }
                                    )
                                }
                            }
                            TaskStatus.COMPLETED -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(EcoPrimaryFixed)
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("TASK FINISHED", style = MaterialTheme.typography.labelLarge, color = EcoOnSurface)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
