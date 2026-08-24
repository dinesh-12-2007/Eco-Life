package com.wasteflow.ecocycle.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wasteflow.ecocycle.data.model.UserRole
import com.wasteflow.ecocycle.ui.components.BrutalistButton
import com.wasteflow.ecocycle.ui.components.BrutalistCard
import com.wasteflow.ecocycle.ui.theme.*

@Composable
fun RoleSelectionScreen(
    onRoleSelected: (UserRole) -> Unit,
    onGetStarted: () -> Unit
) {
    var selectedRole by remember { mutableStateOf(UserRole.CITIZEN) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoSurface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Logo Container
        BrutalistCard(
            modifier = Modifier.size(120.dp),
            backgroundColor = EcoSurfaceContainer,
            shadowOffset = 4.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = "EcoCycle Logo",
                    modifier = Modifier.size(64.dp),
                    tint = EcoPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "ECOCYCLE",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Black,
                fontSize = 36.sp,
                letterSpacing = (-0.02).sp
            ),
            color = EcoOnSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "RECYCLE OR DIE TRYING. CHOOSE YOUR PATH.",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 0.5.sp
            ),
            color = EcoOnSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Role 1: Citizen User
        BrutalistCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = if (selectedRole == UserRole.CITIZEN) EcoPrimaryFixed else EcoSurface,
            shadowOffset = if (selectedRole == UserRole.CITIZEN) 6.dp else 2.dp,
            onClick = {
                selectedRole = UserRole.CITIZEN
                onRoleSelected(UserRole.CITIZEN)
            }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Eco, contentDescription = null, tint = EcoOnSurface)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "CITIZEN USER",
                    style = MaterialTheme.typography.titleLarge,
                    color = EcoOnSurface
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "TRACK & RECYCLE. EARN YOUR STRIPES.",
                style = MaterialTheme.typography.labelSmall,
                color = EcoOnSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Role 2: Service Emp.
        BrutalistCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = if (selectedRole == UserRole.SERVICE_EMPLOYEE) EcoPrimaryFixed else EcoSurface,
            shadowOffset = if (selectedRole == UserRole.SERVICE_EMPLOYEE) 6.dp else 2.dp,
            onClick = {
                selectedRole = UserRole.SERVICE_EMPLOYEE
                onRoleSelected(UserRole.SERVICE_EMPLOYEE)
            }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocalShipping, contentDescription = null, tint = EcoOnSurface)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "SERVICE EMP.",
                    style = MaterialTheme.typography.titleLarge,
                    color = EcoOnSurface
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "MANAGE TASKS. KEEP THE STREETS CLEAN.",
                style = MaterialTheme.typography.labelSmall,
                color = EcoOnSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Role 3: System Mngt.
        BrutalistCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = if (selectedRole == UserRole.SYSTEM_MANAGEMENT) EcoPrimaryFixed else EcoSurface,
            shadowOffset = if (selectedRole == UserRole.SYSTEM_MANAGEMENT) 6.dp else 2.dp,
            onClick = {
                selectedRole = UserRole.SYSTEM_MANAGEMENT
                onRoleSelected(UserRole.SYSTEM_MANAGEMENT)
            }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.SettingsApplications, contentDescription = null, tint = EcoOnSurface)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "SYSTEM MNGT.",
                    style = MaterialTheme.typography.titleLarge,
                    color = EcoOnSurface
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "SYSTEM ANALYTICS. CONTROL THE GRID.",
                style = MaterialTheme.typography.labelSmall,
                color = EcoOnSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        BrutalistButton(
            text = "GET STARTED",
            onClick = onGetStarted,
            backgroundColor = EcoPrimaryContainer,
            contentColor = EcoOnPrimaryContainer
        )
    }
}
