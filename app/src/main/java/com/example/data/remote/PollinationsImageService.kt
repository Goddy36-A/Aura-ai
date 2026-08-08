package com.example.data.remote

import java.net.URLEncoder

/**
 * Generates images via Pollinations.ai \u2014 genuinely free, no API key, no
 * billing account required. Unlike Gemini's image models (which need a
 * paid Google Cloud billing account even for "free tier" access), this
 * just builds a URL: fetching/loading it *is* the generation call.
 *
 * Rate limit on anonymous use is roughly 1 request/15s, which is fine for
 * this app's usage pattern (one image per generated design, not bulk).
 *
 * If you later want to upgrade to Gemini/Imagen once billing is set up,
 * only this file and the couple of callers in StudioRepository need to
 * change \u2014 the UI just renders whatever URL/URI ends up in
 * DesignProject.generatedImageBase64 via Coil, so it doesn't care which
 * provider produced it.
 */
object PollinationsImageService {

    fun buildImageUrl(prompt: String, width: Int = 1024, height: Int = 1024): String {
        val encoded = URLEncoder.encode(prompt, "UTF-8").replace("+", "%20")
        return "https://image.pollinations.ai/prompt/$encoded?width=$width&height=$height"
    }
}
