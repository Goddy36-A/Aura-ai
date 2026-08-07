package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class CoFounderAiService {

    private val tag = "CoFounderAiService"
    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun generateDesignProject(
        category: DesignCategory,
        companyName: String,
        industry: String,
        vibe: String,
        extraNotes: String
    ): Pair<String, String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isNotBlank() && !apiKey.contains("MY_GEMINI") && !apiKey.contains("YOUR_")) {
            try {
                val prompt = """
                    You are a senior AI Co-Founder, Chief Creative Officer, and Chief Strategy Officer for $companyName ($industry, vibe: $vibe).
                    The user wants to generate a high-impact '${category.displayName}'. Extra notes: '$extraNotes'.
                    
                    Return ONLY a valid JSON object with two top-level keys:
                    1. "rationale": A crisp, inspiring 3-sentence executive summary explaining why these design choices and strategic messaging elevate $companyName.
                    2. "spec": A JSON object matching the exact schema for ${category.name}:
                       - If BRAND_KIT: { "companyName": "$companyName", "tagline": "...", "voiceKeywords": ["..."], "primaryColorHex": "#...", "secondaryColorHex": "#...", "accentColorHex": "#...", "darkColorHex": "#...", "lightColorHex": "#...", "displayFont": "...", "bodyFont": "...", "logoConcept": "...", "taglines": ["...", "...", "..."] }
                       - If CERTIFICATE: { "title": "...", "subtitle": "...", "recipientName": "...", "citationText": "...", "dateString": "...", "signatoryName": "...", "signatoryTitle": "...", "sealLabel": "...", "borderStyle": "..." }
                       - If SLIDE_DECK: { "deckTitle": "...", "deckSubtitle": "...", "companyName": "$companyName", "slides": [ { "slideNumber": 1, "title": "...", "subtitle": "...", "bulletPoints": ["...", "..."], "visualCardType": "METRIC" }, ... ] }
                       - If REPORT: { "reportTitle": "...", "companyName": "$companyName", "executiveSummary": "...", "keyMetrics": [ { "label": "...", "value": "...", "description": "..." } ], "strategicPillars": ["...", "..."] }
                       - If COMPANY_GRAPHIC: { "headline": "...", "subheadline": "...", "badgeText": "...", "callToAction": "...", "layoutStyle": "MODERN_BOLD", "keyHighlight": "..." }
                       - If PRODUCT_DESIGN: { "productName": "...", "productCategory": "...", "designTagline": "...", "materialsAndFinish": "...", "targetCustomer": "...", "packagingHighlights": ["...", "..."] }
                       - If POSTER: { "eventTitle": "...", "subtitle": "...", "dateAndLocation": "...", "speakerHighlights": ["...", "..."], "qrCodeLabel": "...", "badgeTheme": "..." }
                """.trimIndent()

                val requestJson = JSONObject().apply {
                    put("contents", JSONArray().put(JSONObject().apply {
                        put("parts", JSONArray().put(JSONObject().apply {
                            put("text", prompt)
                        }))
                    }))
                    put("systemInstruction", JSONObject().apply {
                        put("parts", JSONArray().put(JSONObject().apply {
                            put("text", "You are an executive AI Co-Founder and Creative Design Director.")
                        }))
                    })
                }

                val body = requestJson.toString().toRequestBody("application/json".toMediaType())
                val httpRequest = Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                    .post(body)
                    .build()

                val response = okHttpClient.newCall(httpRequest).execute()
                val responseString = response.body?.string() ?: ""
                val resObj = JSONObject(responseString)
                val candidates = resObj.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val contentObj = firstCandidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    val text = parts?.optJSONObject(0)?.optString("text") ?: ""

                    val cleanJson = extractJson(text)
                    val jsonObj = JSONObject(cleanJson)
                    val rationale = jsonObj.optString("rationale", "Designed to establish category leadership and visual authority.")
                    val specObj = jsonObj.getJSONObject("spec")
                    return@withContext Pair(specObj.toString(), rationale)
                }
            } catch (e: Exception) {
                Log.w(tag, "Gemini REST API error: ${e.message}")
            }
        }

        // Production fallback spec with tailored data
        return@withContext getFallbackDesign(category, companyName, industry, vibe)
    }

    suspend fun generateLogoImage(
        companyName: String,
        logoConcept: String,
        vibe: String,
        primaryColorHex: String,
        secondaryColorHex: String
    ): String? = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey.contains("MY_GEMINI") || apiKey.contains("YOUR_")) {
            return@withContext null
        }
        try {
            val prompt = """
                Design a professional, modern vector-style logo icon for a company called "$companyName".
                Concept direction: $logoConcept.
                Overall brand vibe: $vibe.
                Primary color: $primaryColorHex. Secondary color: $secondaryColorHex.
                The logo should be a clean, centered icon mark suitable for an app icon or brand kit,
                on a plain solid background, no mockup, no extra text other than possibly a short
                wordmark of the company name if it fits naturally. High contrast, professional,
                suitable for a real company brand identity, not a sketch or rough concept drawing.
            """.trimIndent()

            val requestJson = JSONObject().apply {
                put("contents", JSONArray().put(JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().apply {
                        put("text", prompt)
                    }))
                }))
                put("generationConfig", JSONObject().apply {
                    put("responseModalities", JSONArray().put("IMAGE"))
                })
            }

            val body = requestJson.toString().toRequestBody("application/json".toMediaType())
            val httpRequest = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-image-preview:generateContent?key=$apiKey")
                .post(body)
                .build()

            val response = okHttpClient.newCall(httpRequest).execute()
            val responseString = response.body?.string() ?: ""
            val resObj = JSONObject(responseString)
            val candidates = resObj.optJSONArray("candidates") ?: return@withContext null
            if (candidates.length() == 0) return@withContext null

            val parts = candidates.getJSONObject(0)
                .optJSONObject("content")
                ?.optJSONArray("parts") ?: return@withContext null

            for (i in 0 until parts.length()) {
                val part = parts.getJSONObject(i)
                val inlineData = part.optJSONObject("inlineData")
                val data = inlineData?.optString("data")
                if (!data.isNullOrBlank()) {
                    return@withContext data
                }
            }
            null
        } catch (e: Exception) {
            Log.w(tag, "Gemini image generation error: ${e.message}")
            null
        }
    }

    suspend fun chatWithCoFounder(
        userMessage: String,
        persona: PartnerPersona,
        companyContext: String
    ): Pair<String, String?> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isNotBlank() && !apiKey.contains("MY_GEMINI") && !apiKey.contains("YOUR_")) {
            try {
                val prompt = """
                    You are the ${persona.title} (${persona.role}) for an enterprise startup.
                    User asked: "$userMessage"
                    Company context: "$companyContext"
                    
                    Provide a sharp, encouraging, executive-level co-founder response (3-4 sentences max) that combines creative aesthetic insight with strategic business growth.
                    If the user's idea would make a great new design artifact, suggest which category to create by ending your answer with: [CREATE:CATEGORY_NAME] where CATEGORY_NAME is one of: BRAND_KIT, CERTIFICATE, SLIDE_DECK, REPORT, COMPANY_GRAPHIC, PRODUCT_DESIGN, POSTER.
                """.trimIndent()

                val requestJson = JSONObject().apply {
                    put("contents", JSONArray().put(JSONObject().apply {
                        put("parts", JSONArray().put(JSONObject().apply {
                            put("text", prompt)
                        }))
                    }))
                    put("systemInstruction", JSONObject().apply {
                        put("parts", JSONArray().put(JSONObject().apply {
                            put("text", "You are an executive AI founding partner (${persona.title}).")
                        }))
                    })
                }

                val body = requestJson.toString().toRequestBody("application/json".toMediaType())
                val httpRequest = Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                    .post(body)
                    .build()

                val response = okHttpClient.newCall(httpRequest).execute()
                val responseString = response.body?.string() ?: ""
                val resObj = JSONObject(responseString)
                val candidates = resObj.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val contentObj = firstCandidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    val text = parts?.optJSONObject(0)?.optString("text") ?: ""

                    val suggested = extractSuggestedCategory(text)
                    val cleanText = text.replace(Regex("\\[CREATE:[A-Z_]+\\]"), "").trim()
                    if (cleanText.isNotBlank()) {
                        return@withContext Pair(cleanText, suggested)
                    }
                }
            } catch (e: Exception) {
                Log.w(tag, "Gemini REST Chat error: ${e.message}")
            }
        }

        // Smart offline / fallback response
        val fallbackText = when (persona) {
            PartnerPersona.CCO -> {
                "I love this direction! From a creative identity standpoint, we should anchor $companyContext around high-contrast typography and intentional negative space. Let's translate this brand emotion into an executive slide deck or brand kit right now."
            }
            PartnerPersona.CSO -> {
                "Strategically, this positions us ahead of our category peers. Investors respond to visual clarity that reinforces unit economics. Let's formalize this in an Executive Quarterly Report or Seed Pitch Deck."
            }
            PartnerPersona.PRODUCT_ARCHITECT -> {
                "From a product and packaging lens, the physical and digital touchpoints need to feel tactile and premium. I recommend drafting a custom Product Mockup or Founder Certificate of Excellence to solidify our quality standard."
            }
        }
        val suggestedCat = when {
            userMessage.contains("logo", true) || userMessage.contains("brand", true) || userMessage.contains("color", true) -> "BRAND_KIT"
            userMessage.contains("pitch", true) || userMessage.contains("deck", true) || userMessage.contains("investor", true) -> "SLIDE_DECK"
            userMessage.contains("report", true) || userMessage.contains("q3", true) || userMessage.contains("summary", true) -> "REPORT"
            userMessage.contains("award", true) || userMessage.contains("certif", true) -> "CERTIFICATE"
            userMessage.contains("poster", true) || userMessage.contains("event", true) -> "POSTER"
            userMessage.contains("product", true) || userMessage.contains("box", true) -> "PRODUCT_DESIGN"
            else -> null
        }
        Pair(fallbackText, suggestedCat)
    }

    private fun extractJson(text: String): String {
        val start = text.indexOf('{')
        val end = text.lastIndexOf('}')
        if (start != -1 && end != -1 && end > start) {
            return text.substring(start, end + 1)
        }
        return "{}"
    }

    private fun extractSuggestedCategory(text: String): String? {
        val regex = Regex("\\[CREATE:([A-Z_]+)\\]")
        val match = regex.find(text)
        return match?.groupValues?.get(1)
    }

    private fun getFallbackDesign(
        category: DesignCategory,
        companyName: String,
        industry: String,
        vibe: String
    ): Pair<String, String> {
        val rationale = "Selected an authoritative $vibe color palette and modular layout tailored for $industry leadership, ensuring $companyName stands out to investors, customers, and partners alike."
        val jsonStr = when (category) {
            DesignCategory.BRAND_KIT -> {
                val spec = BrandKitSpec(
                    companyName = companyName,
                    tagline = "Architecting the Future of $industry",
                    voiceKeywords = listOf("Bold", "Visionary", "Prestigious", "Trusted"),
                    primaryColorHex = "#4F46E5",
                    secondaryColorHex = "#F59E0B",
                    accentColorHex = "#10B981",
                    darkColorHex = "#090E17",
                    lightColorHex = "#F8FAFC",
                    displayFont = "Playfair Display",
                    bodyFont = "Plus Jakarta Sans",
                    logoConcept = "Minimalist geometric origami diamond with vibrant electric cyan & warm gold facets",
                    taglines = listOf(
                        "Architecting the Future of $industry",
                        "Where Strategic AI Meets Creative Prestige",
                        "Founding Partner for the Modern Visionary"
                    )
                )
                jsonParser.encodeToString(BrandKitSpec.serializer(), spec)
            }
            DesignCategory.CERTIFICATE -> {
                val spec = CertificateSpec(
                    title = "CERTIFICATE OF RECOGNITION",
                    subtitle = "OFFICIAL FOUNDING PARTNER ACHIEVEMENT",
                    recipientName = "Alex Mercer, Founder of $companyName",
                    citationText = "In formal recognition of exceptional leadership, strategic vision, and pioneering contributions to building a category-defining brand in the $industry sector.",
                    dateString = "July 31, 2026",
                    signatoryName = "Vanguard AI Co-Founder",
                    signatoryTitle = "Chief Creative Officer & Studio Partner",
                    sealLabel = "GOLD SEAL",
                    borderStyle = "LUXURY_GOLD"
                )
                jsonParser.encodeToString(CertificateSpec.serializer(), spec)
            }
            DesignCategory.SLIDE_DECK -> {
                val spec = SlideDeckSpec(
                    deckTitle = "$companyName Series A Presentation",
                    deckSubtitle = "Scalable Innovation in $industry",
                    companyName = companyName,
                    slides = listOf(
                        SlideItem(
                            slideNumber = 1,
                            title = "The Market Opportunity in $industry",
                            subtitle = "Why now is the inflection point for $companyName",
                            bulletPoints = listOf(
                                "Legacy solutions in $industry suffer from fragmentation and high latency",
                                "Customers demand cohesive, design-driven experiences",
                                "$companyName delivers 10x efficiency with integrated AI workflows"
                            ),
                            visualCardType = "QUOTE"
                        ),
                        SlideItem(
                            slideNumber = 2,
                            title = "Our Unique Solution & Advantage",
                            subtitle = "Built on creative craftsmanship and strategic velocity",
                            bulletPoints = listOf(
                                "Proprietary $vibe design engine tailored for enterprise scale",
                                "Automated generation of reports, decks, and marketing collateral",
                                "Proven unit economics with 82% gross margins"
                            ),
                            visualCardType = "CHECKLIST"
                        ),
                        SlideItem(
                            slideNumber = 3,
                            title = "Traction & Next Milestone",
                            subtitle = "Accelerating from Seed to Scale",
                            bulletPoints = listOf(
                                "340% YoY ARR growth across 1,200 active client organizations",
                                "4.9/5 satisfaction rating from founder & executive users",
                                "Raising $12M to expand design automation & global sales"
                            ),
                            visualCardType = "METRIC"
                        )
                    )
                )
                jsonParser.encodeToString(SlideDeckSpec.serializer(), spec)
            }
            DesignCategory.REPORT -> {
                val spec = ReportSpec(
                    reportTitle = "Q3 Strategic Growth & Brand Impact Report",
                    companyName = companyName,
                    executiveSummary = "This executive brief evaluates $companyName's momentum within the $industry market. Adopting a $vibe brand identity has significantly elevated customer conversion and investor sentiment.",
                    keyMetrics = listOf(
                        ReportMetric("Brand Recall", "+48%", "Compared to $industry baseline"),
                        ReportMetric("Investor Engagement", "38.5%", "Second meeting conversion rate"),
                        ReportMetric("Design Velocity", "12x Faster", "Automated creative studio workflows")
                    ),
                    strategicPillars = listOf(
                        "1. Uncompromising Visual Polish - Avoiding generic templates through custom M3 layouts",
                        "2. Cohesive Identity Systems - Harmonizing certificates, decks, and graphics",
                        "3. Real-Time Advisory - Continual refinement via CCO & CSO AI Personas"
                    )
                )
                jsonParser.encodeToString(ReportSpec.serializer(), spec)
            }
            DesignCategory.COMPANY_GRAPHIC -> {
                val spec = CompanyGraphicSpec(
                    headline = "ANNOUNCING $companyName LAUNCH",
                    subheadline = "Redefining what's possible in $industry with $vibe design",
                    badgeText = "OFFICIAL LAUNCH",
                    callToAction = "EXPLORE OUR VISION",
                    layoutStyle = "MODERN_BOLD",
                    keyHighlight = "Industry Leadership • 100k+ Active Founders"
                )
                jsonParser.encodeToString(CompanyGraphicSpec.serializer(), spec)
            }
            DesignCategory.PRODUCT_DESIGN -> {
                val spec = ProductDesignSpec(
                    productName = "$companyName Horizon Flagship",
                    productCategory = "Premium $industry Hardware & Digital System",
                    designTagline = "Precision Engineered for Visionaries",
                    materialsAndFinish = "Brushed anodized titanium with sapphire glass accents and matte obsidian gift box packaging",
                    targetCustomer = "Executive founders, architects, and high-performance creators",
                    packagingHighlights = listOf(
                        "Soft-touch embossed gold foil logo emblem",
                        "Magnetic ribbon-pull display box with custom foam inlay",
                        "Includes serialized Founding Partner Certificate of Authenticity"
                    )
                )
                jsonParser.encodeToString(ProductDesignSpec.serializer(), spec)
            }
            DesignCategory.POSTER -> {
                val spec = PosterSpec(
                    eventTitle = "$companyName DESIGN SUMMIT 2026",
                    subtitle = "WHERE STRATEGY MEETS CREATIVE CRAFTSMANSHIP IN ${industry.uppercase()}",
                    dateAndLocation = "OCTOBER 14-16 • SAN FRANCISCO & STREAMING",
                    speakerHighlights = listOf(
                        "Keynote: The AI Co-Founder Revolution in $industry",
                        "Panel: Color Psychology & Brand Prestige",
                        "Workshop: Designing Iconic Company Certificates & Decks"
                    ),
                    qrCodeLabel = "SCAN TO RESERVE VIP PASS",
                    badgeTheme = "ELECTRIC_GOLD"
                )
                jsonParser.encodeToString(PosterSpec.serializer(), spec)
            }
        }
        return Pair(jsonStr, rationale)
    }
}
