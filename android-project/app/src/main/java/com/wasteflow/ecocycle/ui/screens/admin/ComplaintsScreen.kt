package com.wasteflow.ecocycle.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.wasteflow.ecocycle.data.model.Complaint
import com.wasteflow.ecocycle.data.model.ComplaintPriority
import com.wasteflow.ecocycle.data.model.ComplaintStatus
import com.wasteflow.ecocycle.ui.components.BrutalistBadge
import com.wasteflow.ecocycle.ui.components.BrutalistButton
import com.wasteflow.ecocycle.ui.components.BrutalistCard
import com.wasteflow.ecocycle.ui.theme.*

@Composable
fun ComplaintsScreen(
    complaints: List<Complaint>,
    onResolveComplaint: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredComplaints = complaints.filter {
        when (selectedFilter) {
            "PENDING" -> it.status == ComplaintStatus.PENDING
            "ASSIGNED" -> it.status == ComplaintStatus.ASSIGNED
            "RESOLVED" -> it.status == ComplaintStatus.RESOLVED
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoSurface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
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
                    text = "COMPLAINTS",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                    color = EcoOnSurface
                )
            }

            BrutalistBadge(
                text = "4 URGENT",
                backgroundColor = EcoError,
                contentColor = EcoWhite
            )
        }

        // Filter Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("ALL", "PENDING", "ASSIGNED", "RESOLVED").forEach { filter ->
                val isSelected = selectedFilter == filter
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (isSelected) EcoOnSurface else EcoSurface)
                        .border(2.dp, EcoOnSurface)
                        .clickable { selectedFilter = filter }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filter,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isSelected) EcoSurface else EcoOnSurface
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredComplaints) { complaint ->
                val isHighPriority = complaint.priority == ComplaintPriority.HIGH
                val borderColor = if (isHighPriority) EcoError else EcoOnSurface

                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = EcoSurface,
                    borderColor = borderColor,
                    shadowOffset = 6.dp
                ) {
                    // Header ID & Priority
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ID: #${complaint.id}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isHighPriority) EcoError else EcoTertiary
                        )
                        BrutalistBadge(
                            text = if (isHighPriority) "HIGH PRIORITY" else "MEDIUM PRIORITY",
                            backgroundColor = if (isHighPriority) EcoError else EcoTertiary,
                            contentColor = EcoWhite
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = complaint.title.uppercase(),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = EcoOnSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = complaint.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = EcoOnSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = EcoOnSurface, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = complaint.location,
                            style = MaterialTheme.typography.labelMedium.copy(
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                            ),
                            color = EcoOnSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = borderColor, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Assign Crew Row & Resolve Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("ASSIGNED CREW", style = MaterialTheme.typography.labelSmall, color = EcoOnSurfaceVariant)
                            Text(
                                text = complaint.assignedCrew,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = EcoOnSurface
                            )
                        }

                        if (complaint.status != ComplaintStatus.RESOLVED) {
                            Button(
                                onClick = { onResolveComplaint(complaint.id) },
                                modifier = Modifier.border(2.dp, EcoOnSurface),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isHighPriority) EcoError else EcoPrimary,
                                    contentColor = EcoWhite
                                )
                            ) {
                                Text("RESOLVE", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                            }
                        } else {
                            BrutalistBadge(
                                text = "RESOLVED",
                                backgroundColor = EcoPrimaryFixed,
                                contentColor = EcoOnPrimaryFixed
                            )
                        }
                    }
                }
            }
        }
    }
}
