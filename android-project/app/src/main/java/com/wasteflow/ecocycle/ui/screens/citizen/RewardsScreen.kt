package com.wasteflow.ecocycle.ui.screens.citizen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wasteflow.ecocycle.data.model.Reward
import com.wasteflow.ecocycle.data.model.User
import com.wasteflow.ecocycle.ui.components.BrutalistBadge
import com.wasteflow.ecocycle.ui.components.BrutalistButton
import com.wasteflow.ecocycle.ui.components.BrutalistCard
import com.wasteflow.ecocycle.ui.theme.*

@Composable
fun RewardsScreen(
    user: User,
    rewards: List<Reward>,
    onRedeemReward: (Reward) -> Unit,
    onNavigateBack: () -> Unit
) {
    var redemptionMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EcoSurface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = EcoOnSurface)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "REWARDS STORE",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                color = EcoOnSurface
            )
        }

        // Current Balance Banner
        BrutalistCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = EcoPrimaryContainer,
            shadowOffset = 4.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "AVAILABLE BALANCE",
                        style = MaterialTheme.typography.labelSmall,
                        color = EcoOnPrimaryContainer
                    )
                    Text(
                        text = "${user.balancePoints} PTS",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                        color = EcoOnPrimaryContainer
                    )
                }
                Icon(
                    Icons.Default.CardGiftcard,
                    contentDescription = null,
                    tint = EcoOnPrimaryContainer,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        if (redemptionMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EcoPrimaryFixed)
                    .border(2.dp, EcoOnSurface)
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = EcoPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = redemptionMessage!!,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = EcoOnSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "AVAILABLE PERKS",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = EcoOnSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(rewards) { reward ->
                val canAfford = user.balancePoints >= reward.costPoints
                BrutalistCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = EcoSurfaceContainerLowest,
                    shadowOffset = 4.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BrutalistBadge(
                            text = reward.category,
                            backgroundColor = EcoSurfaceContainerHighest,
                            contentColor = EcoOnSurface
                        )
                        BrutalistBadge(
                            text = "${reward.costPoints} PTS",
                            backgroundColor = if (canAfford) EcoPrimaryContainer else EcoSecondaryContainer,
                            contentColor = if (canAfford) EcoOnPrimaryContainer else EcoOnSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = reward.title,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = EcoOnSurface
                    )
                    Text(
                        text = reward.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = EcoOnSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    BrutalistButton(
                        text = if (canAfford) "REDEEM NOW" else "NEED MORE POINTS",
                        onClick = {
                            if (canAfford) {
                                onRedeemReward(reward)
                                redemptionMessage = "Redeemed ${reward.title}! Check your email for voucher code."
                            }
                        },
                        backgroundColor = if (canAfford) EcoPrimary else EcoSecondaryContainer,
                        contentColor = if (canAfford) EcoWhite else EcoOnSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
