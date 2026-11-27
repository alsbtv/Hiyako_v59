package com.hiyako.app.data

enum class WatchStatus { WATCHING, FAVORITE, COMPLETED, PLAN, LATER, DROPPED }

enum class Spoiler { NONE, LOW, MID, HIGH }

data class Anime(
    val id: String,
    val title: String,
    val jp: String,
    val alt: String,
    val year: Int,
    val season: String,
    val type: String,
    val status: String,
    val mal: Double,
    val al: Double,
    val votes: Int,
    val likes: Int,
    val episodes: Int,
    val genres: List<String>,
    val synopsisEn: String,
    val synopsisAr: String,
    val studio: String,
    val country: String,
    val colorA: Long,
    val colorB: Long,
    val motif: String,
    val dow: Int,
    val nextEpHours: Long,
    val adult: Boolean,
    val dub: Boolean
)

data class HistoryEntry(val animeId: String, val ep: Int, val posSec: Int, val durSec: Int, val at: Long)

data class Collection(val id: String, val name: String, val ids: List<String>)

data class NewsItem(val id: String, val ar: String, val en: String, val bodyAr: String, val bodyEn: String, val source: String, val hours: Int, val colorA: Long, val colorB: Long)

data class Character(val id: String, val name: String, val jp: String, val animeId: String, val likes: Int, val colorA: Long, val colorB: Long)

data class ChatMsg(val id: Long, val user: Boolean, val text: String, val spoiler: Spoiler, val model: String)

data class OfflineItem(val animeId: String, val ep: Int, val sizeMb: Int, val at: Long)

data class Notif(val id: String, val ar: String, val en: String, val hours: Int, val read: Boolean, val animeId: String?)
