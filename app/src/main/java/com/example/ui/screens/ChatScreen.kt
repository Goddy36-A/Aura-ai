package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.CoFounderMessage
import com.example.data.models.DesignCategory
import com.example.data.models.PartnerPersona
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.*

@Composable
fun ChatScreen(
    messages: List<CoFounderMessage>,
    activePersona: PartnerPersona,
    companyContext: String,
    onPersonaChange: (PartnerPersona) -> Unit,
    onSendMessage: (String) -> Unit,
    onClearChat: () -> Unit,
    onCreateSuggestedArtifact: (String) -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground)
    ) {
        // Top Persona Selector Bar
        Surface(
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIVE AI CO-FOUNDER BOARD",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = SlateTextMuted,
                            letterSpacing = 1.1.sp
                        )
                    )
                    TextButton(onClick = onClearChat) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Clear",
                            modifier = Modifier.size(16.dp),
                            tint = SlateTextMuted
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Reset Board",
                            style = MaterialTheme.typography.labelSmall.copy(color = SlateTextMuted)
                        )
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(PartnerPersona.entries) { persona ->
                        val isSelected = persona == activePersona
                        val bgColor = if (isSelected) IndigoPrimary else SlateBackground
                        val txtColor = if (isSelected) Color.White else SlateTextDark

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onPersonaChange(persona) },
                            color = bgColor,
                            shadowElevation = if (isSelected) 3.dp else 0.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = when (persona) {
                                        PartnerPersona.CCO -> Icons.Default.Palette
                                        PartnerPersona.CSO -> Icons.Default.TrendingUp
                                        PartnerPersona.PRODUCT_ARCHITECT -> Icons.Default.Inventory2
                                    },
                                    contentDescription = null,
                                    tint = txtColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = persona.name.replace("_", " "),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = txtColor
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Persona Banner
        Surface(
            color = IndigoLight,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = IndigoPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "${activePersona.title} • Focusing on ${activePersona.specialty}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = IndigoPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        // Message List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    onCreateSuggested = onCreateSuggestedArtifact
                )
            }
        }

        // Input Field Bottom Bar
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            text = "Ask ${activePersona.name} for advice or a design concept...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = SlateTextMuted)
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SlateBackground,
                        unfocusedContainerColor = SlateBackground,
                        focusedBorderColor = IndigoPrimary,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (inputText.isNotBlank()) {
                                onSendMessage(inputText)
                                inputText = ""
                            }
                        }
                    )
                )

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            onSendMessage(inputText)
                            inputText = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(IndigoPrimary),
                    enabled = inputText.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: CoFounderMessage,
    onCreateSuggested: (String) -> Unit
) {
    val isUser = message.sender == "user"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (!isUser) {
            Text(
                text = message.persona.replace("_", " "),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = SlateTextMuted,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            )
        }

        Surface(
            color = if (isUser) IndigoPrimary else Color.White,
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = if (isUser) 20.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 20.dp
            ),
            shadowElevation = if (isUser) 0.dp else 2.dp,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isUser) Color.White else SlateTextDark,
                        lineHeight = 20.sp
                    )
                )

                if (!isUser && !message.suggestedCategory.isNullOrEmpty()) {
                    val catName = message.suggestedCategory
                    val (icon, color) = getCategoryIcon(catName)

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onCreateSuggested(catName) },
                        color = color.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = color,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Create ${catName.replace("_", " ")} Now",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = color,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = color,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
