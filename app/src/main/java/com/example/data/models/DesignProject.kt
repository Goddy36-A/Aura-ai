package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
enum class DesignCategory(val displayName: String, val subtitle: String, val iconName: String) {
    BRAND_KIT("Brand Kit", "Logo, Color Palette, Fonts & Taglines", "palette"),
    CERTIFICATE("Certificate & Award", "Official Award, Founder Seal & Signature", "workspace_premium"),
    COMPANY_GRAPHIC("Company Graphic", "Social Banner, Badge & Marketing Card", "image"),
    SLIDE_DECK("Slide Deck", "5-Slide Investor Pitch or Product Showcase", "view_carousel"),
    REPORT("Executive Report", "Structured Quarterly Summary & One-Pager", "assessment"),
    PRODUCT_DESIGN("Product Mockup", "Packaging, Merch & Hardware Framing", "inventory_2"),
    POSTER("Poster & Flyer", "Event Announcement & Launch Poster", "campaign")
}

@Entity(tableName = "design_projects")
@Serializable
data class DesignProject(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val category: String, // name of DesignCategory
    val companyName: String,
    val industry: String = "Technology",
    val vibe: String = "Luxury Minimalist",
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val jsonContent: String = "{}",
    val aiRationale: String = "",
    val generatedImageBase64: String? = null
)
