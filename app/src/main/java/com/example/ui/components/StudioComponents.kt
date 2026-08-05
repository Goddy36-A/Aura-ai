package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.DesignCategory
import com.example.ui.theme.*

@Composable
fun ProfessionalTopBar(
    title: String = "Aura AI",
    subtitle: String = "FOUNDING PARTNER",
    onAvatarClick: () -> Unit = {}
) {
    Surface(
        color = Color.White.copy(alpha = 0.9f),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(IndigoPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Aura AI Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SlateTextDark,
                            fontSize = 18.sp
                        )
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = SlateTextMuted,
                            letterSpacing = 1.2.sp,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF818CF8), Color(0xFF4F46E5))
                        )
                    )
                    .border(2.dp, Color.White, CircleShape)
                    .clickable { onAvatarClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Advisor Settings",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ProfessionalBottomNavBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 12.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                label = "STUDIO",
                icon = Icons.Default.AutoAwesome,
                isSelected = selectedTab == "STUDIO",
                onClick = { onTabSelected("STUDIO") }
            )
            NavItem(
                label = "LIBRARY",
                icon = Icons.Default.Folder,
                isSelected = selectedTab == "LIBRARY",
                onClick = { onTabSelected("LIBRARY") }
            )
            NavItem(
                label = "CHAT",
                icon = Icons.Default.ChatBubbleOutline,
                isSelected = selectedTab == "CHAT",
                onClick = { onTabSelected("CHAT") }
            )
            NavItem(
                label = "ADVISORS",
                icon = Icons.Default.PeopleOutline,
                isSelected = selectedTab == "ADVISORS",
                onClick = { onTabSelected("ADVISORS") }
            )
        }
    }
}

@Composable
private fun NavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) IndigoPrimary else SlateTextMuted
    val bgColor = if (isSelected) IndigoLight else Color.Transparent

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(bgColor)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = contentColor,
                letterSpacing = 0.8.sp
            )
        )
    }
}

@Composable
fun getCategoryIcon(categoryName: String): Pair<ImageVector, Color> {
    return when (categoryName) {
        DesignCategory.BRAND_KIT.name -> Pair(Icons.Default.Palette, AccentEmerald)
        DesignCategory.CERTIFICATE.name -> Pair(Icons.Default.WorkspacePremium, AccentGold)
        DesignCategory.SLIDE_DECK.name -> Pair(Icons.Default.ViewCarousel, AccentBlue)
        DesignCategory.REPORT.name -> Pair(Icons.Default.Assessment, AccentPurple)
        DesignCategory.COMPANY_GRAPHIC.name -> Pair(Icons.Default.Image, AccentOrange)
        DesignCategory.PRODUCT_DESIGN.name -> Pair(Icons.Default.Inventory2, IndigoPrimary)
        DesignCategory.POSTER.name -> Pair(Icons.Default.Campaign, AccentOrange)
        else -> Pair(Icons.Default.AutoAwesome, IndigoPrimary)
    }
}
