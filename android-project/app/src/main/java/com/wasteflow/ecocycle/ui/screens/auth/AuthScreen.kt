package com.wasteflow.ecocycle.ui.screens.auth

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wasteflow.ecocycle.data.model.TokenResponse
import com.wasteflow.ecocycle.ui.components.BrutalistButton
import com.wasteflow.ecocycle.ui.components.BrutalistCard
import com.wasteflow.ecocycle.ui.components.BrutalistShape
import com.wasteflow.ecocycle.ui.theme.*
import com.wasteflow.ecocycle.viewmodel.WasteFlowViewModel

@Composable
fun AuthScreen(
    viewModel: WasteFlowViewModel,
    onLoginSuccess: (TokenResponse) -> Unit
) {
    val authLoading by viewModel.authLoading.collectAsState()
    val authError by viewModel.authError.collectAsState()

    var isLoginTab by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoSurface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "RADICAL ACTION REQUIRED",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Black,
                fontSize = 28.sp,
                letterSpacing = (-0.02).sp
            ),
            color = EcoOnSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Join the movement. Authenticate to track your impact.",
            style = MaterialTheme.typography.bodyMedium,
            color = EcoOnSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = EcoOnSurface, thickness = 2.dp)
        Spacer(modifier = Modifier.height(20.dp))

        // Dual Tab Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(EcoSurface, BrutalistShape)
                .border(2.dp, EcoOnSurface, BrutalistShape)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(if (isLoginTab) EcoPrimaryContainer else Color.Transparent)
                    .border(
                        width = if (isLoginTab) 0.dp else 1.dp,
                        color = if (isLoginTab) Color.Transparent else EcoOnSurface.copy(alpha = 0.3f)
                    )
                    .clickable { isLoginTab = true },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "LOGIN",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isLoginTab) EcoOnPrimaryContainer else EcoOnSurface.copy(alpha = 0.6f)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(if (!isLoginTab) EcoPrimaryContainer else Color.Transparent)
                    .clickable { isLoginTab = false },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SIGNUP",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (!isLoginTab) EcoOnPrimaryContainer else EcoOnSurface.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section badge
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(EcoPrimaryContainer)
                    .border(2.dp, EcoOnSurface)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isLoginTab) "CITIZEN ACCESS" else "NEW RECRUIT",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp),
                color = EcoOnSurface
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isLoginTab) {
            Text(
                text = "FULL NAME",
                style = MaterialTheme.typography.labelSmall,
                color = EcoOnSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("JANE DOE", color = EcoOnSurfaceVariant.copy(alpha = 0.4f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, EcoOnSurface, BrutalistShape),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = EcoSurfaceContainerLowest,
                    unfocusedContainerColor = EcoSurfaceContainerLowest,
                    focusedBorderColor = EcoPrimaryContainer,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = BrutalistShape,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "MOBILE NUMBER",
                style = MaterialTheme.typography.labelSmall,
                color = EcoOnSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                placeholder = { Text("MOBILE NUMBER", color = EcoOnSurfaceVariant.copy(alpha = 0.4f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, EcoOnSurface, BrutalistShape),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = EcoSurfaceContainerLowest,
                    unfocusedContainerColor = EcoSurfaceContainerLowest,
                    focusedBorderColor = EcoPrimaryContainer,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = BrutalistShape,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Text(
            text = "EMAIL ADDRESS",
            style = MaterialTheme.typography.labelSmall,
            color = EcoOnSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("YOUR@EMAIL.COM", color = EcoOnSurfaceVariant.copy(alpha = 0.4f)) },
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, EcoOnSurface, BrutalistShape),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = EcoSurfaceContainerLowest,
                unfocusedContainerColor = EcoSurfaceContainerLowest,
                focusedBorderColor = EcoPrimaryContainer,
                unfocusedBorderColor = Color.Transparent
            ),
            shape = BrutalistShape,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "PASSWORD",
            style = MaterialTheme.typography.labelSmall,
            color = EcoOnSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("••••••••", color = EcoOnSurfaceVariant.copy(alpha = 0.4f)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, EcoOnSurface, BrutalistShape),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = EcoSurfaceContainerLowest,
                unfocusedContainerColor = EcoSurfaceContainerLowest,
                focusedBorderColor = EcoPrimaryContainer,
                unfocusedBorderColor = Color.Transparent
            ),
            shape = BrutalistShape,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        if (isLoginTab) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text(
                    text = "RECOVER ACCESS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                    ),
                    color = EcoOnSurface,
                    modifier = Modifier.clickable { /* Reset password flow */ }
                )
            }
        }

        if (authError != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = authError ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = EcoError
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        BrutalistButton(
            text = if (authLoading) "AUTHENTICATING..." else if (isLoginTab) "ENTER" else "ENLIST",
            onClick = {
                if (!authLoading) {
                    if (isLoginTab) {
                        viewModel.login(
                            email = email,
                            password = password
                        ) { result ->
                            result.getOrNull()?.let { tokenResponse ->
                                onLoginSuccess(tokenResponse)
                            }
                        }
                    } else {
                        viewModel.register(
                            email = email,
                            password = password,
                            name = name,
                            role = "CITIZEN",
                            phone = phone
                        ) { result ->
                            result.getOrNull()?.let { tokenResponse ->
                                onLoginSuccess(tokenResponse)
                            }
                        }
                    }
                }
            },
            backgroundColor = if (isLoginTab) EcoPrimaryContainer else EcoOnSurface,
            contentColor = if (isLoginTab) EcoOnPrimaryContainer else EcoSurface,
            icon = {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = if (isLoginTab) EcoOnPrimaryContainer else EcoSurface
                )
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
        Divider(color = EcoOnSurface, thickness = 2.dp)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "OR AUTHENTICATE VIA NETWORK",
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
            color = EcoOnSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        BrutalistCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = EcoSurface,
            shadowOffset = 4.dp,
            onClick = { /* Google Connect placeholder */ }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GOOGLE CONNECT",
                    style = MaterialTheme.typography.labelLarge,
                    color = EcoOnSurface
                )
            }
        }
    }
}
