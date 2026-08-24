package com.wasteflow.ecocycle.ui.screens.citizen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.UploadFile
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
fun GiveAwayScreen(
    onNavigateBack: () -> Unit,
    onSubmitSuccess: () -> Unit
) {
    var itemTitle by remember { mutableStateOf("") }
    var itemCategory by remember { mutableStateOf("Furniture") }
    var address by remember { mutableStateOf("42 Oak St, Apt 4B") }
    var condition by remember { mutableStateOf("Good (Ready for Reuse)") }
    var isSubmitted by remember { mutableStateOf(false) }

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
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = EcoOnSurface)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "GIVE AWAY ITEM",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                color = EcoOnSurface
            )
        }

        if (isSubmitted) {
            BrutalistCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = EcoPrimaryFixed,
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
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "PICKUP SCHEDULED",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                        color = EcoOnSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Municipal eco-van will collect your items on Tuesday morning. You earned +150 Karma PTS!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = EcoOnSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    BrutalistButton(
                        text = "RETURN HOME",
                        onClick = onSubmitSuccess,
                        backgroundColor = EcoOnSurface,
                        contentColor = EcoSurface
                    )
                }
            }
        } else {
            Text(
                text = "Pass along reusable goods before disposal. Divert waste, earn points.",
                style = MaterialTheme.typography.bodyMedium,
                color = EcoOnSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("1. ITEM NAME", style = MaterialTheme.typography.labelSmall, color = EcoOnSurface)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = itemTitle,
                onValueChange = { itemTitle = it },
                placeholder = { Text("e.g. Wooden Dining Chair, Working Microwave") },
                modifier = Modifier.fillMaxWidth().border(2.dp, EcoOnSurface, BrutalistShape),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = EcoSurfaceContainerLowest,
                    unfocusedContainerColor = EcoSurfaceContainerLowest,
                    focusedBorderColor = EcoPrimaryContainer,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = BrutalistShape
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("2. CATEGORY", style = MaterialTheme.typography.labelSmall, color = EcoOnSurface)
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Furniture", "Electronics", "Textiles", "Tools").forEach { cat ->
                    val isSelected = itemCategory == cat
                    Box(
                        modifier = Modifier
                            .background(if (isSelected) EcoPrimaryContainer else EcoSurface)
                            .border(2.dp, EcoOnSurface)
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) EcoOnPrimaryContainer else EcoOnSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("3. PICKUP ADDRESS", style = MaterialTheme.typography.labelSmall, color = EcoOnSurface)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                modifier = Modifier.fillMaxWidth().border(2.dp, EcoOnSurface, BrutalistShape),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = EcoSurfaceContainerLowest,
                    unfocusedContainerColor = EcoSurfaceContainerLowest,
                    focusedBorderColor = EcoPrimaryContainer,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = BrutalistShape
            )

            Spacer(modifier = Modifier.height(24.dp))

            BrutalistButton(
                text = "SUBMIT GIVEAWAY",
                onClick = { isSubmitted = true },
                backgroundColor = EcoPrimaryContainer,
                contentColor = EcoOnPrimaryContainer
            )
        }
    }
}
