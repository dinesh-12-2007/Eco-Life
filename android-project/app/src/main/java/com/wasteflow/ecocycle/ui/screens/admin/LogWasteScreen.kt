package com.wasteflow.ecocycle.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wasteflow.ecocycle.data.model.WasteType
import com.wasteflow.ecocycle.ui.components.BrutalistButton
import com.wasteflow.ecocycle.ui.components.BrutalistCard
import com.wasteflow.ecocycle.ui.components.BrutalistShape
import com.wasteflow.ecocycle.ui.theme.*

@Composable
fun LogWasteScreen(
    onLogSubmitted: (zone: String, type: WasteType, weight: Double, points: Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedZone by remember { mutableStateOf("Zone B - Residential") }
    var selectedWasteType by remember { mutableStateOf(WasteType.PLASTIC) }
    var weightText by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }

    val weight = weightText.toDoubleOrNull() ?: 0.0
    val pointsCalculated = (weight * selectedWasteType.multiplier).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoSurface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
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

        if (isSuccess) {
            BrutalistCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = EcoSurfaceContainer,
                shadowOffset = 6.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = EcoPrimary,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "ENTRY LOGGED",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                        color = EcoOnSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Data synchronized successfully. +$pointsCalculated PTS credited.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = EcoOnSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    BrutalistButton(
                        text = "NEW LOG",
                        onClick = {
                            isSuccess = false
                            weightText = ""
                        },
                        backgroundColor = EcoPrimary,
                        contentColor = EcoWhite
                    )
                }
            }
        } else {
            Text(
                text = "LOG WASTE",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                color = EcoOnSurface
            )
            Text(
                text = "Record collected materials to calculate impact points.",
                style = MaterialTheme.typography.bodyMedium,
                color = EcoOnSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 1. Select Area
            Text("1. SELECT AREA", style = MaterialTheme.typography.labelLarge, color = EcoOnSurface)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Zone A - Downtown", "Zone B - Residential", "Zone C - Industrial").forEach { zone ->
                    val isSelected = selectedZone == zone
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(if (isSelected) EcoPrimaryContainer else EcoSurfaceContainerHighest)
                            .border(2.dp, EcoOnSurface)
                            .clickable { selectedZone = zone }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = zone.substringBefore(" - "),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) EcoOnPrimaryContainer else EcoOnSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Waste Type
            Text("2. WASTE TYPE", style = MaterialTheme.typography.labelLarge, color = EcoOnSurface)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WasteType.values().take(2).forEach { type ->
                    val isSelected = selectedWasteType == type
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(if (isSelected) EcoPrimary else EcoSurface)
                            .border(2.dp, EcoOnSurface)
                            .clickable { selectedWasteType = type }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = type.title,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected) EcoWhite else EcoOnSurface
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WasteType.values().drop(2).forEach { type ->
                    val isSelected = selectedWasteType == type
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(if (isSelected) EcoPrimary else EcoSurface)
                            .border(2.dp, EcoOnSurface)
                            .clickable { selectedWasteType = type }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = type.title,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected) EcoWhite else EcoOnSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Weight
            Text("3. WEIGHT (KG)", style = MaterialTheme.typography.labelLarge, color = EcoOnSurface)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    placeholder = { Text("0.0", style = MaterialTheme.typography.headlineLarge) },
                    modifier = Modifier
                        .weight(1f)
                        .border(2.dp, EcoOnSurface, BrutalistShape),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = EcoSurface,
                        unfocusedContainerColor = EcoSurface,
                        focusedBorderColor = EcoPrimary,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = BrutalistShape,
                    textStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                // Scan Barcode Simulator
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .fillMaxHeight()
                        .background(EcoSurfaceContainerHigh)
                        .border(2.dp, EcoOnSurface)
                        .clickable {
                            val simulated = (5..35).random() + 0.5
                            weightText = simulated.toString()
                        }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan", tint = EcoOnSurface)
                        Text("SCAN", style = MaterialTheme.typography.labelSmall, color = EcoOnSurface)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Impact Value Preview
            BrutalistCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = EcoPrimaryFixed,
                shadowOffset = 2.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "IMPACT VALUE",
                        style = MaterialTheme.typography.labelLarge,
                        color = EcoOnPrimaryFixed
                    )
                    Text(
                        text = "$pointsCalculated PTS",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                        color = EcoOnPrimaryFixed
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            BrutalistButton(
                text = "SUBMIT DATA",
                onClick = {
                    if (weight > 0) {
                        onLogSubmitted(selectedZone, selectedWasteType, weight, pointsCalculated)
                        isSuccess = true
                    }
                },
                backgroundColor = EcoPrimary,
                contentColor = EcoWhite,
                icon = {
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = EcoWhite)
                }
            )
        }
    }
}
