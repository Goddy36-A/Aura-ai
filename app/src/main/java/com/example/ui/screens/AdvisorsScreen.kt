package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.models.PartnerPersona
import com.example.ui.theme.*

@Composable
fun AdvisorsScreen(
    activePersona: PartnerPersona,
    companyContext: String,
    onPersonaSelected: (PartnerPersona) -> Unit,
    onCompanyContextChange: (String) -> Unit
) {
    var editedCompany by remember(companyContext) { mutableStateOf(companyContext) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground),
        contentPadding = PaddingValues(top = 20.dp, bottom = 96.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Company Context Config Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp)),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ACTIVE VENTURE SETTINGS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = SlateTextMuted,
                                letterSpacing = 1.2.sp
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = null,
                            tint = IndigoPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = "Venture Name & Industry Context",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SlateTextDark
                        )
                    )

                    OutlinedTextField(
                        value = editedCompany,
                        onValueChange = {
                            editedCompany = it
                            onCompanyContextChange(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Company Name / Brief") },
                        placeholder = { Text("e.g., Vanguard AI - Enterprise Design Automation") },
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SlateBackground,
                            unfocusedContainerColor = SlateBackground,
                            focusedBorderColor = IndigoPrimary,
                            unfocusedBorderColor = SlateTextMuted.copy(alpha = 0.3f)
                        ),
                        singleLine = true
                    )
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "EXECUTIVE AI FOUNDING PARTNERS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = SlateTextMuted,
                    letterSpacing = 1.2.sp
                )
            )
        }

        // Advisor Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AdvisorCard(
                    persona = PartnerPersona.CCO,
                    isActive = activePersona == PartnerPersona.CCO,
                    onClick = { onPersonaSelected(PartnerPersona.CCO) }
                )
                AdvisorCard(
                    persona = PartnerPersona.CSO,
                    isActive = activePersona == PartnerPersona.CSO,
                    onClick = { onPersonaSelected(PartnerPersona.CSO) }
                )
                AdvisorCard(
                    persona = PartnerPersona.PRODUCT_ARCHITECT,
                    isActive = activePersona == PartnerPersona.PRODUCT_ARCHITECT,
                    onClick = { onPersonaSelected(PartnerPersona.PRODUCT_ARCHITECT) }
                )
            }
        }
    }
}

@Composable
private fun AdvisorCard(
    persona: PartnerPersona,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isActive) IndigoPrimary else Color.Transparent
    val accentBg = if (isActive) IndigoLight else SlateBackground

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        color = Color.White,
        shadowElevation = if (isActive) 6.dp else 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(accentBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (persona) {
                        PartnerPersona.CCO -> Icons.Default.Palette
                        PartnerPersona.CSO -> Icons.Default.TrendingUp
                        PartnerPersona.PRODUCT_ARCHITECT -> Icons.Default.Inventory2
                    },
                    contentDescription = persona.title,
                    tint = IndigoPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = persona.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SlateTextDark,
                            fontSize = 17.sp
                        )
                    )

                    if (isActive) {
                        Surface(
                            color = IndigoPrimary,
                            shape = CircleShape
                        ) {
                            Text(
                                text = "ACTIVE",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }

                Text(
                    text = persona.role,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = IndigoPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = persona.specialty,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = SlateTextMuted,
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}
