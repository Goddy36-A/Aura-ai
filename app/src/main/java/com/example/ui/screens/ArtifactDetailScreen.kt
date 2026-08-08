package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.*
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.*
import kotlinx.serialization.json.Json

@Composable
fun ArtifactDetailScreen(
    project: DesignProject,
    onBack: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val jsonParser = remember { Json { ignoreUnknownKeys = true; isLenient = true } }
    val (icon, catColor) = getCategoryIcon(project.category)

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                        Text(
                            text = project.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SlateTextDark
                            ),
                            maxLines = 1
                        )
                    }

                    Row {
                        IconButton(onClick = onFavoriteToggle) {
                            Icon(
                                imageVector = if (project.isFavorite) Icons.Default.Star else Icons.Default.StarOutline,
                                contentDescription = "Favorite",
                                tint = if (project.isFavorite) AccentGold else SlateTextMuted
                            )
                        }
                        IconButton(onClick = onDelete) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete",
                                tint = SlateTextMuted
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SlateBackground)
                .padding(paddingValues),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Rationale Card
            item {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = IndigoPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "AI CO-FOUNDER STRATEGIC RATIONALE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = IndigoPrimary,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                        Text(
                            text = project.aiRationale,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = SlateTextDark,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }

            // Artifact Render
            item {
                when (project.category) {
                    DesignCategory.BRAND_KIT.name -> {
                        val spec = try {
                            jsonParser.decodeFromString<BrandKitSpec>(project.jsonContent)
                        } catch (e: Exception) {
                            null
                        }
                        if (spec != null) {
                            BrandKitView(spec = spec, logoImageBase64 = project.generatedImageBase64)
                        } else {
                            FallbackRawJsonView(project.jsonContent)
                        }
                    }
                    DesignCategory.CERTIFICATE.name -> {
                        val spec = try {
                            jsonParser.decodeFromString<CertificateSpec>(project.jsonContent)
                        } catch (e: Exception) { null }
                        if (spec != null) {
                            CertificateView(spec = spec)
                        } else {
                            FallbackRawJsonView(project.jsonContent)
                        }
                    }
                    DesignCategory.SLIDE_DECK.name -> {
                        val spec = try {
                            jsonParser.decodeFromString<SlideDeckSpec>(project.jsonContent)
                        } catch (e: Exception) { null }
                        if (spec != null) {
                            SlideDeckView(spec = spec, coverImageUrl = project.generatedImageBase64)
                        } else {
                            FallbackRawJsonView(project.jsonContent)
                        }
                    }
                    DesignCategory.REPORT.name -> {
                        val spec = try {
                            jsonParser.decodeFromString<ReportSpec>(project.jsonContent)
                        } catch (e: Exception) { null }
                        if (spec != null) {
                            ReportView(spec = spec)
                        } else {
                            FallbackRawJsonView(project.jsonContent)
                        }
                    }
                    DesignCategory.COMPANY_GRAPHIC.name -> {
                        val spec = try {
                            jsonParser.decodeFromString<CompanyGraphicSpec>(project.jsonContent)
                        } catch (e: Exception) { null }
                        if (spec != null) {
                            CompanyGraphicView(spec = spec, imageUrl = project.generatedImageBase64)
                        } else {
                            FallbackRawJsonView(project.jsonContent)
                        }
                    }
                    DesignCategory.PRODUCT_DESIGN.name -> {
                        val spec = try {
                            jsonParser.decodeFromString<ProductDesignSpec>(project.jsonContent)
                        } catch (e: Exception) { null }
                        if (spec != null) {
                            ProductDesignView(spec = spec, imageUrl = project.generatedImageBase64)
                        } else {
                            FallbackRawJsonView(project.jsonContent)
                        }
                    }
                    DesignCategory.POSTER.name -> {
                        val spec = try {
                            jsonParser.decodeFromString<PosterSpec>(project.jsonContent)
                        } catch (e: Exception) { null }
                        if (spec != null) {
                            PosterView(spec = spec, imageUrl = project.generatedImageBase64)
                        } else {
                            FallbackRawJsonView(project.jsonContent)
                        }
                    }
                    else -> {
                        FallbackRawJsonView(project.jsonContent)
                    }
                }
            }
        }
    }
}

@Composable
private fun BrandKitView(spec: BrandKitSpec, logoImageBase64: String?) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Hero Brand Title Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = parseColor(spec.primaryColorHex),
            shape = RoundedCornerShape(24.dp),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = spec.companyName,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = spec.tagline,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White.copy(alpha = 0.9f)
                    )
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    spec.voiceKeywords.forEach { kw ->
                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        ) {
                            Text(
                                text = kw,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.White)
                            )
                        }
                    }
                }
            }
        }

        // Color Palette Swatches
        Text(
            text = "BRAND COLOR PALETTE",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = SlateTextMuted,
                letterSpacing = 1.1.sp
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ColorSwatchCard("Primary", spec.primaryColorHex, Modifier.weight(1f))
            ColorSwatchCard("Secondary", spec.secondaryColorHex, Modifier.weight(1f))
            ColorSwatchCard("Accent", spec.accentColorHex, Modifier.weight(1f))
        }

        // Logo Concept & Typography
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "LOGO CONCEPT & TYPOGRAPHY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = IndigoPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )

                val logoUrl = logoImageBase64
                if (!logoUrl.isNullOrBlank()) {
                    coil.compose.AsyncImage(
                        model = logoUrl,
                        contentDescription = "${spec.companyName} logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = "Logo Concept: ${spec.logoConcept}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = SlateTextDark)
                    )
                }

                Divider(color = SlateBackground)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Display Font", style = MaterialTheme.typography.labelSmall.copy(color = SlateTextMuted))
                        Text(spec.displayFont, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                    Column {
                        Text("Body Font", style = MaterialTheme.typography.labelSmall.copy(color = SlateTextMuted))
                        Text(spec.bodyFont, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

@Composable
private fun GeneratedImageBanner(imageUrl: String?, contentDescription: String) {
    if (imageUrl.isNullOrBlank()) return
    coil.compose.AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun ColorSwatchCard(label: String, hex: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(96.dp),
        color = parseColor(hex),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = hex,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun CertificateView(spec: CertificateSpec) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFFFFBEB), // Elegant parchment tone
        shape = RoundedCornerShape(28.dp),
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = spec.title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF78350F),
                    letterSpacing = 2.sp
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = spec.subtitle,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Color(0xFFB45309),
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp
                )
            )

            Divider(color = Color(0xFFFDE68A), modifier = Modifier.padding(horizontal = 32.dp))

            Text(
                text = "PRESENTED TO",
                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF92400E))
            )

            Text(
                text = spec.recipientName,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF451A03)
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = spec.citationText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF78350F),
                    lineHeight = 22.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(spec.dateString, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF78350F)))
                    Text("Date of Issue", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFB45309)))
                }

                // Award Seal
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD97706)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                        Text("SEAL", style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold))
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(spec.signatoryName, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF78350F)))
                    Text(spec.signatoryTitle, style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFB45309)))
                }
            }
        }
    }
}

@Composable
private fun SlideDeckView(spec: SlideDeckSpec, coverImageUrl: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        GeneratedImageBanner(imageUrl = coverImageUrl, contentDescription = "${spec.deckTitle} cover art")

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = IndigoDark,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(spec.companyName, style = MaterialTheme.typography.labelSmall.copy(color = AccentGold, fontWeight = FontWeight.Bold))
                Text(spec.deckTitle, style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Bold))
                Text(spec.deckSubtitle, style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.8f)))
            }
        }

        spec.slides.forEach { slide ->
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "SLIDE ${slide.slideNumber}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = IndigoPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = slide.visualCardType,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SlateTextMuted
                            )
                        )
                    }

                    Text(
                        text = slide.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SlateTextDark
                        )
                    )
                    Text(
                        text = slide.subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(color = SlateTextMuted)
                    )

                    Divider(color = SlateBackground)

                    slide.bulletPoints.forEach { point ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("•", style = MaterialTheme.typography.bodyMedium.copy(color = IndigoPrimary, fontWeight = FontWeight.Bold))
                            Text(
                                text = point,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = SlateTextDark,
                                    lineHeight = 20.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportView(spec: ReportSpec) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(spec.companyName, style = MaterialTheme.typography.labelSmall.copy(color = IndigoPrimary, fontWeight = FontWeight.Bold))
                Text(spec.reportTitle, style = MaterialTheme.typography.titleLarge.copy(color = SlateTextDark, fontWeight = FontWeight.Bold))
                Text(
                    text = spec.executiveSummary,
                    style = MaterialTheme.typography.bodyMedium.copy(color = SlateTextMuted, lineHeight = 20.sp)
                )
            }
        }

        // KPI Metric Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            spec.keyMetrics.take(3).forEach { metric ->
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(metric.value, style = MaterialTheme.typography.titleLarge.copy(color = IndigoPrimary, fontWeight = FontWeight.Bold))
                        Text(metric.label, style = MaterialTheme.typography.labelSmall.copy(color = SlateTextDark, fontWeight = FontWeight.Bold))
                        Text(metric.description, style = MaterialTheme.typography.labelSmall.copy(color = SlateTextMuted, fontSize = 10.sp))
                    }
                }
            }
        }

        // Strategic Pillars
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("STRATEGIC PILLARS", style = MaterialTheme.typography.labelSmall.copy(color = IndigoPrimary, fontWeight = FontWeight.Bold))
                spec.strategicPillars.forEach { pillar ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = AccentEmerald, modifier = Modifier.size(18.dp))
                        Text(pillar, style = MaterialTheme.typography.bodyMedium.copy(color = SlateTextDark))
                    }
                }
            }
        }
    }
}

@Composable
private fun CompanyGraphicView(spec: CompanyGraphicSpec, imageUrl: String? = null) {
  Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
    GeneratedImageBanner(imageUrl = imageUrl, contentDescription = "${spec.headline} graphic")
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = IndigoDark,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                color = AccentOrange,
                shape = CircleShape
            ) {
                Text(
                    text = spec.badgeText,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontWeight = FontWeight.Bold)
                )
            }

            Text(
                text = spec.headline,
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White, fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )

            Text(
                text = spec.subheadline,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.85f)),
                textAlign = TextAlign.Center
            )

            Text(
                text = spec.keyHighlight,
                style = MaterialTheme.typography.labelMedium.copy(color = AccentGold, fontWeight = FontWeight.Bold)
            )

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = IndigoDark),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(spec.callToAction, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
        }
    }
  }
}

@Composable
private fun ProductDesignView(spec: ProductDesignSpec, imageUrl: String? = null) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GeneratedImageBanner(imageUrl = imageUrl, contentDescription = "${spec.productName} mockup")
            Text(spec.productCategory, style = MaterialTheme.typography.labelSmall.copy(color = IndigoPrimary, fontWeight = FontWeight.Bold))
            Text(spec.productName, style = MaterialTheme.typography.titleLarge.copy(color = SlateTextDark, fontWeight = FontWeight.Bold))
            Text(spec.designTagline, style = MaterialTheme.typography.bodyMedium.copy(color = SlateTextMuted))

            Divider(color = SlateBackground)

            Text("Materials & Finish", style = MaterialTheme.typography.labelSmall.copy(color = SlateTextMuted, fontWeight = FontWeight.Bold))
            Text(spec.materialsAndFinish, style = MaterialTheme.typography.bodyMedium.copy(color = SlateTextDark))

            Text("Packaging & Unboxing Highlights", style = MaterialTheme.typography.labelSmall.copy(color = SlateTextMuted, fontWeight = FontWeight.Bold))
            spec.packagingHighlights.forEach { item ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("•", style = MaterialTheme.typography.bodyMedium.copy(color = IndigoPrimary, fontWeight = FontWeight.Bold))
                    Text(item, style = MaterialTheme.typography.bodyMedium.copy(color = SlateTextDark))
                }
            }
        }
    }
}

@Composable
private fun PosterView(spec: PosterSpec, imageUrl: String? = null) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF1E293B),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            GeneratedImageBanner(imageUrl = imageUrl, contentDescription = "${spec.eventTitle} poster art")
            Text(spec.subtitle, style = MaterialTheme.typography.labelSmall.copy(color = AccentGold, fontWeight = FontWeight.Bold))
            Text(spec.eventTitle, style = MaterialTheme.typography.headlineSmall.copy(color = Color.White, fontWeight = FontWeight.Bold))
            Text(spec.dateAndLocation, style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f)))

            Divider(color = Color.White.copy(alpha = 0.15f))

            Text("KEY HIGHLIGHTS & SPEAKERS", style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold))
            spec.speakerHighlights.forEach { spk ->
                Text("• $spk", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White))
            }

            Surface(
                color = IndigoPrimary,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = spec.qrCodeLabel,
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.labelMedium.copy(color = Color.White, fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun FallbackRawJsonView(jsonString: String) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("SPECIFICATION DATA", style = MaterialTheme.typography.labelSmall.copy(color = IndigoPrimary, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = jsonString,
                style = MaterialTheme.typography.bodySmall.copy(color = SlateTextDark, fontFamily = FontFamily.Monospace)
            )
        }
    }
}

private fun parseColor(hex: String): Color {
    return try {
        val cleaned = hex.removePrefix("#")
        val colorInt = cleaned.toLong(16)
        if (cleaned.length == 6) {
            Color(colorInt or 0xFF000000)
        } else {
            Color(colorInt)
        }
    } catch (e: Exception) {
        IndigoPrimary
    }
}
