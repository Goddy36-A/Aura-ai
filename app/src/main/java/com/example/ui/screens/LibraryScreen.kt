package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.DesignCategory
import com.example.data.models.DesignProject
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.*

@Composable
fun LibraryScreen(
    projects: List<DesignProject>,
    selectedCategory: String?,
    onSelectCategoryFilter: (String?) -> Unit,
    onProjectClick: (DesignProject) -> Unit,
    onFavoriteToggle: (DesignProject) -> Unit,
    onDeleteProject: (DesignProject) -> Unit
) {
    val filters = listOf(
        "ALL" to "All Artifacts",
        DesignCategory.BRAND_KIT.name to "Brand Kits",
        DesignCategory.CERTIFICATE.name to "Certificates",
        DesignCategory.SLIDE_DECK.name to "Slide Decks",
        DesignCategory.REPORT.name to "Reports",
        DesignCategory.COMPANY_GRAPHIC.name to "Graphics",
        DesignCategory.PRODUCT_DESIGN.name to "Products",
        DesignCategory.POSTER.name to "Posters"
    )

    val filteredProjects = if (selectedCategory == null || selectedCategory == "ALL") {
        projects
    } else {
        projects.filter { it.category == selectedCategory }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground)
    ) {
        // Top Filter Bar
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { (key, label) ->
                val isSelected = (selectedCategory == key) || (selectedCategory == null && key == "ALL")
                val bgColor = if (isSelected) IndigoPrimary else Color.White
                val txtColor = if (isSelected) Color.White else SlateTextDark

                Surface(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onSelectCategoryFilter(key) },
                    color = bgColor,
                    shadowElevation = if (isSelected) 4.dp else 1.dp
                ) {
                    Text(
                        text = label,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = txtColor
                        )
                    )
                }
            }
        }

        // Project Cards
        if (filteredProjects.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FolderOpen,
                        contentDescription = "Empty",
                        tint = SlateTextMuted,
                        modifier = Modifier.size(56.dp)
                    )
                    Text(
                        text = "No artifacts in this category yet.",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = SlateTextDark,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Use the AI Studio to generate brand kits, decks, and reports.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = SlateTextMuted)
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredProjects) { project ->
                    LibraryProjectCard(
                        project = project,
                        onClick = { onProjectClick(project) },
                        onFavoriteClick = { onFavoriteToggle(project) },
                        onDeleteClick = { onDeleteProject(project) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryProjectCard(
    project: DesignProject,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val (icon, color) = getCategoryIcon(project.category)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!project.generatedImageBase64.isNullOrBlank()) {
                coil.compose.AsyncImage(
                    model = project.generatedImageBase64,
                    contentDescription = "${project.title} thumbnail",
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(14.dp))
                )
            }
            // Header: Category chip + Favorite + Delete
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
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = project.category.replace("_", " "),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = color,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (project.isFavorite) Icons.Default.Star else Icons.Default.StarOutline,
                            contentDescription = "Favorite",
                            tint = if (project.isFavorite) AccentGold else SlateTextMuted
                        )
                    }
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete",
                            tint = SlateTextMuted
                        )
                    }
                }
            }

            // Title and Company
            Column {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SlateTextDark,
                        fontSize = 17.sp
                    )
                )
                Text(
                    text = "${project.companyName} • ${project.industry} • ${project.vibe}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = SlateTextMuted,
                        fontSize = 12.sp
                    )
                )
            }

            // Rationale Box
            Surface(
                color = SlateBackground,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Rationale",
                        tint = IndigoPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = project.aiRationale,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = SlateTextDark,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}
