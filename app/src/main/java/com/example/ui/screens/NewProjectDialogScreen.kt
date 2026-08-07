package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.models.DesignCategory
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.*

@Composable
fun NewProjectDialogScreen(
    initialCategory: DesignCategory,
    defaultCompanyName: String,
    onDismiss: () -> Unit,
    onGenerate: (DesignCategory, String, String, String, String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var companyName by remember { mutableStateOf(defaultCompanyName) }
    var industry by remember { mutableStateOf("AI Enterprise & Tech") }
    var vibe by remember { mutableStateOf("Luxury Minimalist") }
    var extraNotes by remember { mutableStateOf("") }

    val vibeOptions = listOf(
        "Luxury Minimalist",
        "Corporate Bold",
        "Prestigious Gold",
        "Modern Electric",
        "Editorial Monochrome"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(28.dp)),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "NEW STUDIO ARTIFACT",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SlateTextDark
                        )
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Category Selector
                Text(
                    text = "SELECT CATEGORY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = SlateTextMuted
                    )
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(DesignCategory.entries) { cat ->
                        val isSelected = cat == selectedCategory
                        val (icon, color) = getCategoryIcon(cat.name)
                        val bgColor = if (isSelected) IndigoPrimary else SlateBackground
                        val txtColor = if (isSelected) Color.White else SlateTextDark

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { selectedCategory = cat },
                            color = bgColor
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else color,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = cat.displayName,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = txtColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }

                // Inputs
                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Venture Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = SlateTextDark,
                        unfocusedTextColor = SlateTextDark
                    )
                )

                OutlinedTextField(
                    value = industry,
                    onValueChange = { industry = it },
                    label = { Text("Industry / Domain") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = SlateTextDark,
                        unfocusedTextColor = SlateTextDark
                    )
                )

                Text(
                    text = "BRAND AESTHETIC VIBE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = SlateTextMuted
                    )
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(vibeOptions) { option ->
                        val isSelected = option == vibe
                        val bgColor = if (isSelected) IndigoLight else SlateBackground
                        val txtColor = if (isSelected) IndigoPrimary else SlateTextDark

                        Surface(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { vibe = option },
                            color = bgColor
                        ) {
                            Text(
                                text = option,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = txtColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = extraNotes,
                    onValueChange = { extraNotes = it },
                    label = { Text("Custom Notes or Specific Focus (Optional)") },
                    placeholder = { Text("e.g. Include metrics about 10x ROI for series A deck") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = SlateTextDark,
                        unfocusedTextColor = SlateTextDark
                    )
                )

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (companyName.isNotBlank()) {
                                onGenerate(selectedCategory, companyName, industry, vibe, extraNotes)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Generate Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
