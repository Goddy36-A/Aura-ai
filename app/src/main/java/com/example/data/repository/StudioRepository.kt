package com.example.data.repository

import com.example.data.local.ChatDao
import com.example.data.local.DesignDao
import com.example.data.models.*
import com.example.data.remote.CoFounderAiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

class StudioRepository(
    private val designDao: DesignDao,
    private val chatDao: ChatDao,
    private val aiService: CoFounderAiService
) {
    val allProjects: Flow<List<DesignProject>> = designDao.getAllProjects()
    val allMessages: Flow<List<CoFounderMessage>> = chatDao.getAllMessages()
    private val jsonParser = Json { ignoreUnknownKeys = true }

    suspend fun ensureInitialDemoProjects() {
        val current = allProjects.first()
        if (current.isEmpty()) {
            // 1. Brand Kit Demo
            val brandKitSpec = BrandKitSpec(
                companyName = "Vanguard AI",
                tagline = "Architecting the Future of Enterprise Design",
                voiceKeywords = listOf("Bold", "Visionary", "Prestigious"),
                primaryColorHex = "#4F46E5",
                secondaryColorHex = "#F59E0B",
                accentColorHex = "#10B981",
                darkColorHex = "#090E17",
                lightColorHex = "#F8FAFC",
                displayFont = "Playfair Display",
                bodyFont = "Plus Jakarta Sans",
                logoConcept = "Minimalist origami diamond with glowing gold facets",
                taglines = listOf(
                    "Architecting the Future of Enterprise Design",
                    "Where Strategic AI Meets Creative Prestige",
                    "Founding Partner for the Modern Visionary"
                )
            )
            designDao.insertProject(
                DesignProject(
                    title = "Vanguard AI - Executive Brand Kit",
                    category = DesignCategory.BRAND_KIT.name,
                    companyName = "Vanguard AI",
                    industry = "AI Studio",
                    vibe = "Luxury Minimalist",
                    isFavorite = true,
                    jsonContent = jsonParser.encodeToString(BrandKitSpec.serializer(), brandKitSpec),
                    aiRationale = "Harmonizes deep indigo authority with warm gold highlights to evoke both engineering precision and executive prestige."
                )
            )

            // 2. Certificate Demo
            val certSpec = CertificateSpec(
                title = "CERTIFICATE OF RECOGNITION",
                subtitle = "OFFICIAL FOUNDING PARTNER ACHIEVEMENT",
                recipientName = "Alex Mercer",
                citationText = "In recognition of exceptional leadership, strategic vision, and pioneering contributions to building a world-class enterprise brand and product ecosystem.",
                dateString = "July 31, 2026",
                signatoryName = "Aura AI Co-Founder",
                signatoryTitle = "Chief Creative Officer & Studio Partner",
                sealLabel = "GOLD SEAL",
                borderStyle = "LUXURY_GOLD"
            )
            designDao.insertProject(
                DesignProject(
                    title = "Founding Partner Certificate of Honor",
                    category = DesignCategory.CERTIFICATE.name,
                    companyName = "Vanguard AI",
                    industry = "AI Studio",
                    vibe = "Prestigious Award",
                    isFavorite = true,
                    jsonContent = jsonParser.encodeToString(CertificateSpec.serializer(), certSpec),
                    aiRationale = "Designed with classic typographic proportions and a gold seal badge to commemorate founder achievements."
                )
            )

            // 3. Slide Deck Demo
            val deckSpec = SlideDeckSpec(
                deckTitle = "Seed Stage Investor Presentation",
                deckSubtitle = "Building a Scalable AI Enterprise Studio",
                companyName = "Vanguard AI",
                slides = listOf(
                    SlideItem(
                        slideNumber = 1,
                        title = "The Problem & Opportunity",
                        subtitle = "Founders struggle to bridge creative identity and strategic pitch",
                        bulletPoints = listOf(
                            "Traditional branding studios take 6+ weeks and cost \$20,000+",
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
            designDao.insertProject(
                DesignProject(
                    title = "Vanguard AI Series A Investor Deck",
                    category = DesignCategory.SLIDE_DECK.name,
                    companyName = "Vanguard AI",
                    industry = "AI Studio",
                    vibe = "Corporate Bold",
                    isFavorite = false,
                    jsonContent = jsonParser.encodeToString(SlideDeckSpec.serializer(), deckSpec),
                    aiRationale = "Structured for maximum narrative momentum: problem, solution, and hard traction metrics."
                )
            )

            // 4. Report Demo
            val reportSpec = ReportSpec(
                reportTitle = "Q3 Enterprise Strategy & Brand Impact Report",
                companyName = "Vanguard AI",
                executiveSummary = "This report evaluates the intersection of strategic brand positioning and visual cohesion across our upcoming product launch. Strong visual prestige has driven a 42% lift in investor response rates.",
                keyMetrics = listOf(
                    ReportMetric("Brand Recall", "+48%", "Compared to industry baseline"),
                    ReportMetric("Pitch Deck Conversion", "34.2%", "Lead to second investor meeting"),
                    ReportMetric("Design Velocity", "12x Faster", "Automated creative studio workflows")
                ),
                strategicPillars = listOf(
                    "1. Uncompromising Visual Polish - Avoiding generic templates through custom M3 layouts",
                    "2. Cohesive Identity Systems - Harmonizing certificates, decks, and graphics",
                    "3. Real-Time Advisory - Continual refinement via CCO & CSO AI Personas"
                )
            )
            designDao.insertProject(
                DesignProject(
                    title = "Q3 Executive Brand & Strategy Brief",
                    category = DesignCategory.REPORT.name,
                    companyName = "Vanguard AI",
                    industry = "AI Studio",
                    vibe = "Executive Formal",
                    isFavorite = false,
                    jsonContent = jsonParser.encodeToString(ReportSpec.serializer(), reportSpec),
                    aiRationale = "Presents data with scannable KPI cards and clear strategic pillars for stakeholder review."
                )
            )

            // Add welcome chat message from CCO
            chatDao.insertMessage(
                CoFounderMessage(
                    sender = "partner",
                    text = "Welcome to your Creative Studio! I'm your AI Chief Creative Officer. We can generate entire Brand Kits, Certificates, Investor Slide Decks, Executive Reports, or Product Designs. What shall we build for your venture today?",
                    persona = PartnerPersona.CCO.name
                )
            )
        }
    }

    suspend fun createProjectWithAi(
        title: String,
        category: DesignCategory,
        companyName: String,
        industry: String,
        vibe: String,
        extraNotes: String
    ): Int {
        val (jsonSpec, rationale) = aiService.generateDesignProject(
            category = category,
            companyName = companyName,
            industry = industry,
            vibe = vibe,
            extraNotes = extraNotes
        )

        // Build a tailored image prompt per category. Categories that are
        // fundamentally text/structured documents (Certificate, Report)
        // stay text-only for now \u2014 an image there would be decorative
        // rather than the actual content.
        var artifactImageUrl: String? = null
        try {
            val imagePrompt: String? = when (category) {
                DesignCategory.BRAND_KIT -> {
                    val spec = jsonParser.decodeFromString(BrandKitSpec.serializer(), jsonSpec)
                    "Professional modern vector-style logo icon for a company called " +
                        "\"${spec.companyName}\". Concept: ${spec.logoConcept}. Brand vibe: $vibe. " +
                        "Primary color ${spec.primaryColorHex}, secondary color ${spec.secondaryColorHex}. " +
                        "Clean centered icon mark on a plain solid background, no mockup, high contrast, " +
                        "professional brand identity quality, not a rough sketch."
                }
                DesignCategory.POSTER -> {
                    val spec = jsonParser.decodeFromString(PosterSpec.serializer(), jsonSpec)
                    "Professional event poster background art for \"${spec.eventTitle}\", " +
                        "${spec.subtitle}. Style: $vibe, theme ${spec.badgeTheme}. Bold modern " +
                        "graphic design, no readable text in the image, abstract shapes and " +
                        "lighting suitable as a poster backdrop for $companyName."
                }
                DesignCategory.COMPANY_GRAPHIC -> {
                    val spec = jsonParser.decodeFromString(CompanyGraphicSpec.serializer(), jsonSpec)
                    "Modern company announcement banner background graphic, layout style " +
                        "${spec.layoutStyle}, brand vibe $vibe, for $companyName in the $industry " +
                        "industry. Bold abstract shapes, no readable text in the image, professional " +
                        "marketing graphic quality."
                }
                DesignCategory.PRODUCT_DESIGN -> {
                    val spec = jsonParser.decodeFromString(ProductDesignSpec.serializer(), jsonSpec)
                    "Premium product mockup photo of \"${spec.productName}\", ${spec.productCategory}. " +
                        "Materials and finish: ${spec.materialsAndFinish}. Studio product photography, " +
                        "$vibe aesthetic, clean background, professional catalog quality."
                }
                DesignCategory.SLIDE_DECK -> {
                    val spec = jsonParser.decodeFromString(SlideDeckSpec.serializer(), jsonSpec)
                    "Investor pitch deck cover slide background art for \"${spec.deckTitle}\", " +
                        "${spec.deckSubtitle}, for $companyName. Style: $vibe. Abstract professional " +
                        "presentation background, no readable text in the image."
                }
                else -> null
            }
            if (imagePrompt != null) {
                artifactImageUrl = aiService.generateArtifactImage(imagePrompt)
            }
        } catch (e: Exception) {
            // Spec parsing or image URL build failed \u2014 fall back gracefully
            // to text-only content, don't block project creation over it.
        }

        val project = DesignProject(
            title = title.ifEmpty { "$companyName - ${category.displayName}" },
            category = category.name,
            companyName = companyName,
            industry = industry,
            vibe = vibe,
            jsonContent = jsonSpec,
            aiRationale = rationale,
            generatedImageBase64 = artifactImageUrl
        )
        return designDao.insertProject(project).toInt()
    }

    suspend fun toggleFavorite(id: Int, currentFav: Boolean) {
        designDao.updateFavoriteStatus(id, !currentFav)
    }

    suspend fun deleteProject(id: Int) {
        designDao.deleteProjectById(id)
    }

    suspend fun sendChatMessage(userText: String, persona: PartnerPersona, companyContext: String) {
        chatDao.insertMessage(
            CoFounderMessage(
                sender = "user",
                text = userText,
                persona = persona.name
            )
        )
        val (replyText, suggestedCat) = aiService.chatWithCoFounder(
            userMessage = userText,
            persona = persona,
            companyContext = companyContext
        )
        chatDao.insertMessage(
            CoFounderMessage(
                sender = "partner",
                text = replyText,
                persona = persona.name,
                suggestedCategory = suggestedCat
            )
        )
    }

    suspend fun clearChat() {
        chatDao.clearAllMessages()
    }
}
