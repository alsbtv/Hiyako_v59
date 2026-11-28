package com.hiyako.app.data

private val genreAr = mapOf(
    "Action" to "أكشن",
    "Adventure" to "مغامرة",
    "Comedy" to "كوميديا",
    "Drama" to "دراما",
    "Fantasy" to "فانتازيا",
    "Horror" to "رعب",
    "Mystery" to "غموض",
    "Psychological" to "نفسي",
    "Sci-Fi" to "خيال علمي",
    "Slice of Life" to "شريحة من الحياة",
    "Sports" to "رياضة",
    "Supernatural" to "خارق للطبيعة",
    "School" to "مدرسي",
    "Music" to "موسيقى",
    "Historical" to "تاريخي"
)

fun genreLabel(g: String, ar: Boolean): String = if (ar) (genreAr[g] ?: g) else g

fun normalizeText(s: String): String {
    val sb = StringBuilder()
    for (ch in s.lowercase()) {
        val c = ch.code
        if (c in 0x064B..0x065F || c == 0x0670 || c == 0x0640) continue
        val mapped = when (ch) {
            'أ', 'إ', 'آ', 'ٱ' -> 'ا'
            'ى' -> 'ي'
            'ة' -> 'ه'
            'ؤ' -> 'و'
            'ئ' -> 'ي'
            else -> ch
        }
        sb.append(mapped)
    }
    return sb.toString()
        .replace(Regex("[^\\p{L}\\p{N}\\s]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}

fun editDistance(a: String, b: String, max: Int): Int {
    if (kotlin.math.abs(a.length - b.length) > max) return max + 1
    var prev = IntArray(b.length + 1) { it }
    var cur = IntArray(b.length + 1)
    for (i in 1..a.length) {
        cur[0] = i
        for (j in 1..b.length) {
            val cost = if (a[i - 1] == b[j - 1]) 0 else 1
            cur[j] = minOf(prev[j] + 1, cur[j - 1] + 1, prev[j - 1] + cost)
        }
        val swap = prev
        prev = cur
        cur = swap
    }
    return prev[b.length]
}

private fun tolerance(len: Int): Int = when {
    len <= 3 -> 0
    len <= 6 -> 1
    else -> 2
}

private fun nameFields(a: Anime): List<String> {
    val raw = listOf(a.title, a.jp, a.alt, a.studio) + a.genres + a.genres.map { genreLabel(it, true) }
    return raw.map { normalizeText(it) }.filter { it.isNotEmpty() }
}

/** 0 means no match; a higher number means a better match. */
fun matchScore(a: Anime, query: String): Int {
    val q = normalizeText(query)
    if (q.isEmpty()) return 1
    val names = nameFields(a)
    val words = names.flatMap { it.split(" ") }.filter { it.isNotEmpty() }
    val synopsis = normalizeText(a.synopsisEn + " " + a.synopsisAr)
    var total = 0
    for (tok in q.split(" ")) {
        var best = 0
        for (n in names) {
            if (n == tok) best = maxOf(best, 100)
            else if (n.startsWith(tok)) best = maxOf(best, 80)
            else if (n.contains(tok)) best = maxOf(best, 60)
        }
        if (best == 0) {
            for (w in words) {
                if (w.startsWith(tok)) {
                    best = maxOf(best, 70)
                } else if (tok.length >= 3) {
                    val tol = tolerance(tok.length)
                    val d = minOf(editDistance(w, tok, tol), editDistance(w.take(tok.length), tok, tol))
                    if (d <= tol) best = maxOf(best, 50 - d * 5)
                }
            }
        }
        if (best == 0 && tok.length >= 3 && synopsis.contains(tok)) best = 20
        if (best == 0) return 0
        total += best
    }
    return total
}
