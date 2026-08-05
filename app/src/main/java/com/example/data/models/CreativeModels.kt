package com.example.data.models

import kotlinx.serialization.Serializable

// Structured representation of generated design content
@Serializable
data class BrandKitSpec(
    val companyName: String = "Vanguard AI",
    val tagline: String = "Architecting the Future of Enterprise Design",
    val voiceKeywords: List<String> = listOf("Bold", "Visionary", "Prestigious"),
    val primaryColorHex: String = "#38BDF8", // Electric Blue
    val secondaryColorHex: String = "#F59E0B", // Warm Gold
    val accentColorHex: String = "#10B981",  // Emerald Accent
    val darkColorHex: String = "#090E17",    // Obsidian Twilight
    val lightColorHex: String = "#F8FAFC",   // Crisp Off-White
    val displayFont: String = "Playfair Display",
    val bodyFont: String = "Plus Jakarta Sans",
    val logoConcept: String = "Minimalist origami diamond with glowing gold facets",
    val taglines: List<String> = listOf(
        "Architecting the Future of Enterprise Design",
        "Where Strategic AI Meets Creative Prestige",
        "Founding Partner for the Modern Visionary"
    )
)

@Serializable
data class CertificateSpec(
    val title: String = "CERTIFICATE OF RECOGNITION",
    val subtitle: String = "OFFICIAL FOUNDING PARTNER ACHIEVEMENT",
    val recipientName: String = "Alex Mercer",
    val citationText: String = "In recognition of exceptional leadership, strategic vision, and pioneering contributions to building a world-class enterprise brand and product ecosystem.",
    val dateString: String = "July 31, 2026",
    val signatoryName: String = "Vanguard AI Co-Founder",
    val signatoryTitle: String = "Chief Creative Officer & Studio Partner",
    val sealLabel: String = "GOLD SEAL",
    val borderStyle: String = "LUXURY_GOLD"
)

@Serializable
data class SlideItem(
    val slideNumber: Int,
    val title: String,
    val subtitle: String,
    val bulletPoints: List<String>,
    val visualCardType: String = "METRIC" // "METRIC", "QUOTE", "CHECKLIST", "COMPARISON"
)

@Serializable
data class SlideDeckSpec(
    val deckTitle: String = "Seed Stage Investor Presentation",
    val deckSubtitle: String = "Building a Scalable AI Enterprise Studio",
    val companyName: String = "Vanguard Studio",
    val slides: List<SlideItem> = listOf(
        SlideItem(
            slideNumber = 1,
            title = "The Problem & Opportunity",
            subtitle = "Founders struggle to bridge creative identity and strategic pitch",
            bulletPoints = listOf(
                "Traditional branding studios take 6+ weeks and cost $20,000+",
                "Generic AI design tools lack executive storytelling context",
                "Founders need an AI partner that aligns design with business strategy"
            ),
            visualCardType = "QUOTE"
        ),
        SlideItem(
            slideNumber = 2,
            title = "Our Solution: AI Founding Partner",
            subtitle = "An integrated creative studio + strategic co-founder",
            bulletPoints = listOf(
                "Instant Brand Kit generation with bespoke color psychology",
                "Executive reports, certificates, and investor decks in seconds",
                "Live CCO & CSO AI personas that critique and refine every artifact"
            ),
            visualCardType = "CHECKLIST"
        ),
        SlideItem(
            slideNumber = 3,
            title = "Market Traction & Growth",
            subtitle = "Accelerating from Seed to Scale",
            bulletPoints = listOf(
                "4.9/5 Founder Satisfaction across 1,200+ ventures",
                "85% reduction in time-to-launch for new company visuals",
                "Designed for mobile-first founders and creative directors"
            ),
            visualCardType = "METRIC"
        )
    )
)

@Serializable
data class ReportSpec(
    val reportTitle: String = "Q3 Enterprise Strategy & Brand Impact Report",
    val companyName: String = "Vanguard Studio",
    val executiveSummary: String = "This report evaluates the intersection of strategic brand positioning and visual cohesion across our upcoming product launch. Strong visual prestige has driven a 42% lift in investor response rates.",
    val keyMetrics: List<ReportMetric> = listOf(
        ReportMetric("Brand Recall", "+48%", "Compared to industry baseline"),
        ReportMetric("Pitch Deck Conversion", "34.2%", "Lead to second investor meeting"),
        ReportMetric("Design Velocity", "12x Faster", "Automated creative studio workflows")
    ),
    val strategicPillars: List<String> = listOf(
        "1. Uncompromising Visual Polish - Avoiding generic templates through custom M3 layouts",
        "2. Cohesive Identity Systems - Harmonizing certificates, decks, and graphics",
        "3. Real-Time Advisory - Continual refinement via CCO & CSO AI Personas"
    )
)

@Serializable
data class ReportMetric(
    val label: String,
    val value: String,
    val description: String
)

@Serializable
data class CompanyGraphicSpec(
    val headline: String = "ANNOUNCING OUR SERIES A LAUNCH",
    val subheadline: String = "Empowering the next generation of creative founders",
    val badgeText: String = "OFFICIAL RELEASE",
    val callToAction: String = "EXPLORE VANGUARD AI STUDIO",
    val layoutStyle: String = "MODERN_BOLD", // "MODERN_BOLD", "LUXURY_CARD", "NEON_CYBER"
    val keyHighlight: String = "$15M Funding Secured • 100k+ Founders"
)

@Serializable
data class ProductDesignSpec(
    val productName: String = "Vanguard Horizon Pro",
    val productCategory: String = "Premium Hardware Mockup & Packaging",
    val designTagline: String = "Precision Engineered for Visionaries",
    val materialsAndFinish: String = "Brushed anodized titanium with sapphire glass accents and matte obsidian gift box packaging",
    val targetCustomer: String = "Executive founders, architects, and high-performance creators",
    val packagingHighlights: List<String> = listOf(
        "Soft-touch embossed gold foil logo emblem",
        "Magnetic ribbon-pull display box with custom foam inlay",
        "Includes serialized Founding Partner Certificate of Authenticity"
    )
)

@Serializable
data class PosterSpec(
    val eventTitle: String = "DESIGN FUTURE SUMMIT 2026",
    val subtitle: String = "WHERE STRATEGY MEETS CREATIVE CRAFTSMANSHIP",
    val dateAndLocation: String = "OCTOBER 14-16 • SAN FRANCISCO & STREAMING",
    val speakerHighlights: List<String> = listOf(
        "Keynote: The AI Co-Founder Revolution",
        "Panel: Color Psychology in Venture Branding",
        "Workshop: Designing Iconic Company Certificates & Decks"
    ),
    val qrCodeLabel: String = "SCAN TO RESERVE VIP PASS",
    val badgeTheme: String = "ELECTRIC_GOLD"
)
