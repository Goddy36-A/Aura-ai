package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.DesignCategory
import com.example.data.models.DesignProject
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.*

@Composable
fun StudioHomeScreen(
    projects: List<DesignProject>,
    companyContext: String,
    onCategoryClick: (DesignCategory) -> Unit,
    onProjectClick: (DesignProject) -> Unit,
    onStartNewProjectClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground),
        contentPadding = PaddingValues(bottom = 96.dp, top = 16.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Hero Partner Status Card (Professional Polish theme)
        item {
            HeroStatusCard(
                companyContext = companyContext,
                onContinueClick = {
                    val latest = projects.firstOrNull()
                    if (latest != null) {
                        onProjectClick(latest)
                    } else {
                        onCategoryClick(DesignCategory.SLIDE_DECK)
                    }
                }
            )
        }

        // Quick Actions Grid Header
        item {
            Text(
                text = "CREATE NEW ARTIFACT",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = SlateTextMuted,
                    letterSpacing = 1.2.sp
                )
            )
        }

        // Quick Actions Grid (2 columns x 3 rows)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategoryCard(
                        category = DesignCategory.BRAND_KIT,
                        modifier = Modifier.weight(1f),
                        onClick = { onCategoryClick(DesignCategory.BRAND_KIT) }
                    )
                    CategoryCard(
                        category = DesignCategory.SLIDE_DECK,
                        modifier = Modifier.weight(1f),
                        onClick = { onCategoryClick(DesignCategory.SLIDE_DECK) }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategoryCard(
                        category = DesignCategory.CERTIFICATE,
                        modifier = Modifier.weight(1f),
                        onClick = { onCategoryClick(DesignCategory.CERTIFICATE) }
                    )
                    CategoryCard(
                        category = DesignCategory.REPORT,
                        modifier = Modifier.weight(1f),
                        onClick = { onCategoryClick(DesignCategory.REPORT) }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategoryCard(
                        category = DesignCategory.COMPANY_GRAPHIC,
                        modifier = Modifier.weight(1f),
                        onClick = { onCategoryClick(DesignCategory.COMPANY_GRAPHIC) }
                    )
                    CategoryCard(
                        category = DesignCategory.PRODUCT_DESIGN,
                        modifier = Modifier.weight(1f),
                        onClick = { onCategoryClick(DesignCategory.PRODUCT_DESIGN) }
                    )
                }
            }
        }

        // Recent Artifacts Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RECENT STUDIO ARTIFACTS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = SlateTextMuted,
                        letterSpacing = 1.2.sp
                    )
                )
                Text(
                    text = "${projects.size} items",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = IndigoPrimary
                    )
                )
            }
        }

        // Recent Projects Horizontal Showcase
        if (projects.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No artifacts generated yet. Click any category above to begin!",
                        style = MaterialTheme.typography.bodyMedium.copy(color = SlateTextMuted)
                    )
                }
            }
        } else {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(projects) { project ->
                        RecentProjectCard(
                            project = project,
                            onClick = { onProjectClick(project) }
                        )
                    }
                }
            }
        }

        // Bottom Quick Create Activity Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onStartNewProjectClick() },
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(IndigoLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "New Artifact",
                            tint = IndigoPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Start Bespoke AI Project",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SlateTextDark
                            )
                        )
                        Text(
                            text = "Custom color psychology, executive deck, or award seal",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = SlateTextMuted
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SlateBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Proceed",
                            tint = IndigoPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroStatusCard(
    companyContext: String,
    onContinueClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(
                Brush.linearGradient(
                    listOf(IndigoDark, Color(0xFF311B92))
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = CircleShape
                ) {
                    Text(
                        text = "Status: Active AI Co-Founder",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI Partner",
                    tint = AccentGold,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column {
                Text(
                    text = "Ready to build for",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = "$companyContext?",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Chief Creative & Strategy Advisory",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.6f)
                    )
                )
                Button(
                    onClick = onContinueClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = IndigoDark
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Continue",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(
    category: DesignCategory,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val (icon, color) = getCategoryIcon(category.name)
    val bgColor = color.copy(alpha = 0.12f)

    Surface(
        modifier = modifier
            .height(104.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = category.displayName,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column {
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = SlateTextDark,
                        fontSize = 14.sp
                    ),
                    maxLines = 1
                )
                Text(
                    text = category.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = SlateTextMuted,
                        fontSize = 11.sp
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun RecentProjectCard(
    project: DesignProject,
    onClick: () -> Unit
) {
    val (icon, color) = getCategoryIcon(project.category)

    Surface(
        modifier = Modifier
            .width(230.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        color = Color.White,
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = color.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = project.category.replace("_", " "),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = color,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                if (project.isFavorite) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favorite",
                        tint = AccentGold,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Column {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SlateTextDark,
                        fontSize = 15.sp
                    ),
                    maxLines = 1
                )
                Text(
                    text = project.companyName,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = SlateTextMuted,
                        fontSize = 12.sp
                    ),
                    maxLines = 1
                )
            }

            Text(
                text = project.aiRationale.take(64) + if (project.aiRationale.length > 64) "..." else "",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = SlateTextMuted,
                    fontSize = 11.sp
                ),
                maxLines = 2
            )
        }
    }
}
