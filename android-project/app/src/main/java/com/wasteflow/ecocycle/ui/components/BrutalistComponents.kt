package com.wasteflow.ecocycle.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wasteflow.ecocycle.ui.theme.*

val BrutalistShape = RoundedCornerShape(2.dp)

@Composable
fun BrutalistCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = EcoSurfaceContainerLowest,
    borderColor: Color = EcoOnSurface,
    borderWidth: Dp = 2.dp,
    shadowOffset: Dp = 4.dp,
    shape: Shape = BrutalistShape,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val currentOffset by animateDpAsState(
        targetValue = if (isPressed && onClick != null) 1.dp else shadowOffset,
        label = "brutalistShadow"
    )

    Box(modifier = modifier) {
        // Hard Black Shadow Layer
        if (shadowOffset > 0.dp) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = currentOffset, y = currentOffset)
                    .background(borderColor, shape)
            )
        }

        // Main Surface Box
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, shape)
                .border(borderWidth, borderColor, shape)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick
                        )
                    } else Modifier
                )
                .padding(16.dp),
            content = content
        )
    }
}

@Composable
fun BrutalistButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = EcoPrimaryContainer,
    contentColor: Color = EcoOnPrimaryContainer,
    borderColor: Color = EcoOnSurface,
    icon: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val shadowOffset by animateDpAsState(
        targetValue = if (isPressed) 0.dp else 4.dp,
        label = "btnShadow"
    )

    Box(modifier = modifier) {
        // Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .background(borderColor, BrutalistShape)
        )
        // Button Surface
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, BrutalistShape)
                .border(2.dp, borderColor, BrutalistShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
                .padding(vertical = 14.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = contentColor
            )
            if (icon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                icon()
            }
        }
    }
}

@Composable
fun BrutalistBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = EcoOnSurface,
    contentColor: Color = EcoSurface,
    borderColor: Color = EcoOnSurface
) {
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(1.dp))
            .border(1.dp, borderColor, RoundedCornerShape(1.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp,
                letterSpacing = 1.sp
            ),
            color = contentColor
        )
    }
}
