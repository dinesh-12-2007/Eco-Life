package com.wasteflow.ecocycle.ui.screens.citizen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wasteflow.ecocycle.data.model.*
import com.wasteflow.ecocycle.ui.components.BrutalistBadge
import com.wasteflow.ecocycle.ui.components.BrutalistButton
import com.wasteflow.ecocycle.ui.components.BrutalistCard
import com.wasteflow.ecocycle.ui.theme.*
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElectricityBillScreen(
    user: User,
    providers: List<ElectricityProvider>,
    conversionConfig: PointsConversionConfig,
    paymentHistory: List<BillPaymentReceipt>,
    onFetchBill: (providerId: String, consumerNumber: String) -> ElectricityBill,
    onPayBill: (
        providerId: String,
        consumerNumber: String,
        billNumber: String,
        totalAmount: Double,
        pointsToRedeem: Int,
        onResult: (Result<BillPaymentReceipt>) -> Unit
    ) -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedProvider by remember {
        mutableStateOf(providers.firstOrNull() ?: ElectricityProvider("prov-01", "BESCOM (Bangalore Electricity)", "Karnataka", "BESCOM"))
    }
    var providerDropdownExpanded by remember { mutableStateOf(false) }
    var consumerNumberInput by remember { mutableStateOf("90283471") }
    var fetchedBill by remember { mutableStateOf<ElectricityBill?>(null) }
    var pointsToUseInput by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successReceipt by remember { mutableStateOf<BillPaymentReceipt?>(null) }
    var isProcessing by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf(0) } // 0: Pay Bill, 1: Payment History

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoSurface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        // Screen Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = EcoOnSurface)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "ELECTRICITY BILL PAYMENT",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                    color = EcoOnSurface
                )
                Text(
                    text = "Redeem eco points for municipal utility credits",
                    style = MaterialTheme.typography.bodySmall,
                    color = EcoOnSurfaceVariant
                )
            }
        }

        // Segmented Tab Switcher (Pay Bill vs History)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(EcoSurfaceContainer)
                .border(2.dp, EcoOnSurface)
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (activeTab == 0) EcoOnSurface else Color.Transparent)
                    .clickable { activeTab = 0 }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PAY NEW BILL",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (activeTab == 0) EcoWhite else EcoOnSurface
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (activeTab == 1) EcoOnSurface else Color.Transparent)
                    .clickable { activeTab = 1 }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PAYMENT HISTORY (${paymentHistory.size})",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (activeTab == 1) EcoWhite else EcoOnSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (activeTab == 1) {
            // Payment History View
            if (paymentHistory.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No electricity payments recorded yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = EcoOnSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(paymentHistory) { receipt ->
                        BrutalistCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = EcoSurfaceContainerLowest,
                            shadowOffset = 3.dp
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BrutalistBadge(
                                    text = receipt.status,
                                    backgroundColor = EcoPrimaryContainer,
                                    contentColor = EcoOnPrimaryContainer
                                )
                                Text(
                                    text = receipt.timestamp,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = EcoOnSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = receipt.providerName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = EcoOnSurface
                            )
                            Text(
                                text = "Consumer #${receipt.consumerNumber} • Bill #${receipt.billNumber}",
                                style = MaterialTheme.typography.bodySmall,
                                color = EcoOnSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = EcoOnSurface.copy(alpha = 0.1f), thickness = 1.dp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "POINTS REDEEMED",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = EcoOnSurfaceVariant
                                    )
                                    Text(
                                        text = "-${receipt.pointsRedeemed} PTS (${conversionConfig.currencySymbol}${String.format("%.2f", receipt.pointsDiscountAmount)})",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                        color = EcoPrimary
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "NET AMOUNT PAID",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = EcoOnSurfaceVariant
                                    )
                                    Text(
                                        text = "${conversionConfig.currencySymbol}${String.format("%.2f", receipt.amountPaid)}",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                        color = EcoOnSurface
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Ref: ${receipt.transactionRef}",
                                style = MaterialTheme.typography.labelSmall,
                                color = EcoOnSurfaceVariant
                            )
                        }
                    }
                }
            }
        } else {
            // Pay Bill Flow
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                // Wallet Balance & Conversion Rate Callout
                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = EcoPrimaryContainer,
                    shadowOffset = 3.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "AVAILABLE REWARD WALLET",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = EcoOnPrimaryContainer
                            )
                            Text(
                                text = "${user.balancePoints} PTS",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                                color = EcoOnPrimaryContainer
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "CONVERSION VALUE",
                                style = MaterialTheme.typography.labelSmall,
                                color = EcoOnPrimaryContainer
                            )
                            Text(
                                text = "${conversionConfig.currencySymbol}${String.format("%.2f", user.balancePoints.toDouble() / conversionConfig.pointsPerUnit)}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = EcoOnPrimaryContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "⚡ ${conversionConfig.description}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = EcoOnPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Form: Provider & Consumer Number
                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = EcoSurfaceContainerLowest,
                    shadowOffset = 3.dp
                ) {
                    Text(
                        text = "1. ENTER ELECTRICITY BILL DETAILS",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black),
                        color = EcoOnSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Provider Selector
                    Text(
                        text = "Select Electricity Board / Provider",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = EcoOnSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    ExposedDropdownMenuBox(
                        expanded = providerDropdownExpanded,
                        onExpandedChange = { providerDropdownExpanded = !providerDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedProvider.name,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = providerDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EcoOnSurface,
                                unfocusedBorderColor = EcoOnSurface.copy(alpha = 0.5f)
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = providerDropdownExpanded,
                            onDismissRequest = { providerDropdownExpanded = false }
                        ) {
                            providers.forEach { prov ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(prov.name, fontWeight = FontWeight.Bold)
                                            Text(prov.state, style = MaterialTheme.typography.bodySmall)
                                        }
                                    },
                                    onClick = {
                                        selectedProvider = prov
                                        providerDropdownExpanded = false
                                        fetchedBill = null
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Consumer Number Field
                    Text(
                        text = "Consumer / Service Connection Number",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = EcoOnSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = consumerNumberInput,
                        onValueChange = {
                            consumerNumberInput = it.filter { char -> char.isLetterOrDigit() }
                            fetchedBill = null
                        },
                        placeholder = { Text("e.g. 90283471") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EcoOnSurface,
                            unfocusedBorderColor = EcoOnSurface.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    BrutalistButton(
                        text = "FETCH BILL DETAILS",
                        onClick = {
                            if (consumerNumberInput.isBlank()) {
                                errorMessage = "Please enter a valid consumer number."
                            } else {
                                errorMessage = null
                                fetchedBill = onFetchBill(selectedProvider.id, consumerNumberInput)
                                // Pre-fill with maximum usable points
                                val maxPointsForBill = (fetchedBill!!.billAmount * conversionConfig.pointsPerUnit).toInt()
                                pointsToUseInput = min(user.balancePoints, maxPointsForBill)
                            }
                        },
                        backgroundColor = EcoOnSurface,
                        contentColor = EcoWhite
                    )
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(EcoErrorContainer)
                            .border(2.dp, EcoError)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = errorMessage!!,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = EcoOnErrorContainer
                        )
                    }
                }

                // Display Fetched Bill Information & Points Redemption
                if (fetchedBill != null) {
                    val bill = fetchedBill!!
                    val maxPointsAllowed = (bill.billAmount * conversionConfig.pointsPerUnit).toInt()
                    val maxRedeemablePoints = min(user.balancePoints, maxPointsAllowed)
                    val discount = pointsToUseInput.toDouble() / conversionConfig.pointsPerUnit
                    val netPayable = (bill.billAmount - discount).coerceAtLeast(0.0)

                    Spacer(modifier = Modifier.height(14.dp))

                    BrutalistCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = EcoSurfaceContainerLowest,
                        shadowOffset = 3.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "2. BILL INFORMATION",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black),
                                color = EcoOnSurface
                            )
                            BrutalistBadge(
                                text = bill.status,
                                backgroundColor = EcoErrorContainer,
                                contentColor = EcoOnErrorContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Consumer Name", style = MaterialTheme.typography.labelSmall, color = EcoOnSurfaceVariant)
                                Text(bill.consumerName, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Bill Number", style = MaterialTheme.typography.labelSmall, color = EcoOnSurfaceVariant)
                                Text(bill.billNumber, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Billing Cycle", style = MaterialTheme.typography.labelSmall, color = EcoOnSurfaceVariant)
                                Text(bill.billingMonth, style = MaterialTheme.typography.bodyMedium)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Due Date", style = MaterialTheme.typography.labelSmall, color = EcoOnSurfaceVariant)
                                Text(bill.dueDate, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = EcoError)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = EcoOnSurface.copy(alpha = 0.1f), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TOTAL BILL AMOUNT:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = EcoOnSurface
                            )
                            Text(
                                text = "${conversionConfig.currencySymbol}${String.format("%.2f", bill.billAmount)}",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                color = EcoOnSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3. Choose Reward Points to Redeem
                    BrutalistCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = EcoPrimaryFixed,
                        shadowOffset = 3.dp
                    ) {
                        Text(
                            text = "3. REDEEM REWARD POINTS",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black),
                            color = EcoOnSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Choose points to apply as direct electricity credit discount.",
                            style = MaterialTheme.typography.bodySmall,
                            color = EcoOnSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "POINTS TO REDEEM:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = EcoOnSurface
                            )
                            Text(
                                text = "$pointsToUseInput PTS",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                color = EcoPrimary
                            )
                        }

                        if (maxRedeemablePoints > 0) {
                            Slider(
                                value = pointsToUseInput.toFloat(),
                                onValueChange = { pointsToUseInput = it.toInt() },
                                valueRange = 0f..maxRedeemablePoints.toFloat(),
                                steps = if (maxRedeemablePoints > 10) 9 else 0,
                                colors = SliderDefaults.colors(
                                    thumbColor = EcoPrimary,
                                    activeTrackColor = EcoPrimary,
                                    inactiveTrackColor = EcoSurfaceContainerHigh
                                )
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            BrutalistButton(
                                text = "USE NONE (0)",
                                onClick = { pointsToUseInput = 0 },
                                backgroundColor = EcoSurface,
                                contentColor = EcoOnSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            BrutalistButton(
                                text = "USE MAX ($maxRedeemablePoints PTS)",
                                onClick = { pointsToUseInput = maxRedeemablePoints },
                                backgroundColor = EcoPrimary,
                                contentColor = EcoWhite,
                                modifier = Modifier.weight(1.5f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = EcoOnSurface.copy(alpha = 0.2f), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Bill Calculation Summary
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Original Bill Amount", style = MaterialTheme.typography.bodySmall)
                            Text("${conversionConfig.currencySymbol}${String.format("%.2f", bill.billAmount)}", style = MaterialTheme.typography.bodySmall)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Points Discount (-$pointsToUseInput PTS)", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = EcoPrimary)
                            Text("-${conversionConfig.currencySymbol}${String.format("%.2f", discount)}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = EcoPrimary)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("NET REMAINING PAYABLE:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black))
                            Text(
                                "${conversionConfig.currencySymbol}${String.format("%.2f", netPayable)}",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                color = EcoOnSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4. Confirm Payment
                    BrutalistButton(
                        text = if (isProcessing) "PROCESSING SETTLEMENT..." else "CONFIRM & PAY BILL",
                        onClick = {
                            if (!isProcessing) {
                                isProcessing = true
                                errorMessage = null
                                onPayBill(
                                    selectedProvider.id,
                                    bill.consumerNumber,
                                    bill.billNumber,
                                    bill.billAmount,
                                    pointsToUseInput
                                ) { result ->
                                    isProcessing = false
                                    result.onSuccess { receipt ->
                                        successReceipt = receipt
                                        fetchedBill = null
                                    }.onFailure { ex ->
                                        errorMessage = ex.message ?: "Failed to process electricity bill payment."
                                    }
                                }
                            }
                        },
                        backgroundColor = EcoPrimary,
                        contentColor = EcoWhite,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Success Receipt Dialog / Card
                if (successReceipt != null) {
                    val receipt = successReceipt!!
                    Spacer(modifier = Modifier.height(16.dp))
                    BrutalistCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = EcoSurfaceContainerLowest,
                        shadowOffset = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EcoPrimary, modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "PAYMENT SUCCESSFUL",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                    color = EcoPrimary
                                )
                            }
                            BrutalistBadge(text = "PAID", backgroundColor = EcoPrimaryContainer, contentColor = EcoOnPrimaryContainer)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Your electricity bill payment has been successfully recorded in the municipal grid ledger.",
                            style = MaterialTheme.typography.bodySmall,
                            color = EcoOnSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = EcoOnSurface.copy(alpha = 0.1f), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Transaction ID: ${receipt.transactionRef}", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                        Text("Provider: ${receipt.providerName}", style = MaterialTheme.typography.bodySmall)
                        Text("Consumer #: ${receipt.consumerNumber}", style = MaterialTheme.typography.bodySmall)
                        Text("Bill #: ${receipt.billNumber}", style = MaterialTheme.typography.bodySmall)
                        Text("Points Redeemed: ${receipt.pointsRedeemed} PTS (${conversionConfig.currencySymbol}${String.format("%.2f", receipt.pointsDiscountAmount)})", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = EcoPrimary)
                        Text("Paid Amount: ${conversionConfig.currencySymbol}${String.format("%.2f", receipt.amountPaid)}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        Text("New Wallet Balance: ${receipt.updatedWalletBalance} PTS", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))

                        Spacer(modifier = Modifier.height(12.dp))
                        BrutalistButton(
                            text = "DONE / PAY ANOTHER BILL",
                            onClick = { successReceipt = null },
                            backgroundColor = EcoOnSurface,
                            contentColor = EcoWhite
                        )
                    }
                }
            }
        }
    }
}
