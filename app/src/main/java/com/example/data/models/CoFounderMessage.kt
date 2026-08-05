package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
enum class PartnerPersona(val title: String, val role: String, val specialty: String) {
    CCO("Chief Creative Officer", "Visual Identity & Brand Emotion", "Color psychology, aesthetics, logo presence"),
    CSO("Chief Strategy Officer", "Market Pitch & Investor Storytelling", "Go-to-market, pitch decks, value proposition"),
    PRODUCT_ARCHITECT("Product Design Architect", "UX/UI & Packaging Systems", "Product framing, mockups, user journey")
}

@Entity(tableName = "cofounder_messages")
@Serializable
data class CoFounderMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sender: String, // "user" or "partner"
    val text: String,
    val persona: String = PartnerPersona.CCO.name,
    val timestamp: Long = System.currentTimeMillis(),
    val suggestedCategory: String? = null // e.g. "BRAND_KIT" if AI suggests turning idea into design
)
