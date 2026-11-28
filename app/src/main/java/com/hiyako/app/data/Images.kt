package com.hiyako.app.data

/**
 * Image links per anime, taken from trusted sources (AniList CDN).
 * An empty entry means the link is not filled yet: the app shows a plain colored placeholder.
 * Fill the maps with lines like:  "frieren" to "https://...jpg",
 */
object Pics {
    private val banners: Map<String, String> = mapOf(
    )

    private val posters: Map<String, String> = mapOf(
    )

    fun banner(id: String): String = banners[id].orEmpty()

    fun poster(id: String): String = posters[id].orEmpty()

    fun gallery(id: String): List<String> = listOf(banner(id), poster(id)).filter { it.isNotEmpty() }
}
