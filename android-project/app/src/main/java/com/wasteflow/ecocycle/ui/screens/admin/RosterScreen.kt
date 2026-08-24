package com.wasteflow.ecocycle.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.wasteflow.ecocycle.data.model.Employee
import com.wasteflow.ecocycle.ui.components.BrutalistBadge
import com.wasteflow.ecocycle.ui.components.BrutalistButton
import com.wasteflow.ecocycle.ui.components.BrutalistCard
import com.wasteflow.ecocycle.ui.components.BrutalistShape
import com.wasteflow.ecocycle.ui.theme.*

@Composable
fun RosterScreen(
    employees: List<Employee>,
    onNavigateBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredEmployees = employees.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.zone.contains(searchQuery, ignoreCase = true)
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
        Row(verticalAlignment = Alignment.CenterVertically) {
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

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    "SEARCH EMPLOYEE ID OR NAME",
                    style = MaterialTheme.typography.labelMedium,
                    color = EcoOnSurfaceVariant
                )
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = EcoOnSurface)
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, EcoOnSurface, BrutalistShape),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = EcoSurfaceContainerLowest,
                unfocusedContainerColor = EcoSurfaceContainerLowest,
                focusedBorderColor = EcoPrimary,
                unfocusedBorderColor = Color.Transparent
            ),
            shape = BrutalistShape,
            singleLine = true
        )

        Text(
            text = "ACTIVE ROSTER",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
            color = EcoOnSurface
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredEmployees) { employee ->
                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = EcoSurfaceContainerLowest,
                    shadowOffset = 4.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar placeholder
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(EcoSurfaceContainerHighest)
                                .border(2.dp, EcoOnSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = EcoOnSurface)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = employee.name.uppercase(),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = EcoOnSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                BrutalistBadge(
                                    text = employee.zone,
                                    backgroundColor = EcoOnSurface,
                                    contentColor = EcoWhite
                                )
                                BrutalistBadge(
                                    text = employee.status,
                                    backgroundColor = if (employee.status == "ON-DUTY") EcoPrimaryFixed else EcoSurfaceContainerHighest,
                                    contentColor = EcoOnSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = EcoOnSurface, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("PERFORMANCE", style = MaterialTheme.typography.labelSmall, color = EcoOnSurfaceVariant)
                            Text(
                                text = "${employee.performance}/5",
                                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                                color = EcoOnSurface
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("TASKS DONE", style = MaterialTheme.typography.labelSmall, color = EcoOnSurfaceVariant)
                            Text(
                                text = "${employee.tasksDone}",
                                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                                color = EcoOnSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    BrutalistButton(
                        text = "ASSIGN TASK",
                        onClick = { /* Task assign flow */ },
                        backgroundColor = EcoPrimaryContainer,
                        contentColor = EcoOnPrimaryContainer
                    )
                }
            }

            // Leaderboard Section at bottom
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "LEADERBOARD",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                        color = EcoOnSurface
                    )
                    Text("THIS WEEK", style = MaterialTheme.typography.labelSmall, color = EcoOnSurfaceVariant)
                }
                Spacer(modifier = Modifier.height(8.dp))

                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = EcoPrimaryContainer,
                    shadowOffset = 2.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "1",
                            style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
                            color = EcoOnPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "SARAH JENKINS",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = EcoOnPrimaryContainer
                            )
                            Text("142 TASKS", style = MaterialTheme.typography.labelSmall, color = EcoOnPrimaryContainer)
                        }
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = EcoOnPrimaryContainer, modifier = Modifier.size(32.dp))
                    }
                }
            }
        }
    }
}
