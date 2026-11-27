package com.hiyako.app.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow

data class Snack(val msg: String, val undo: (() -> Unit)? = null)

object Store {
    private lateinit var prefs: SharedPreferences

    val language = MutableStateFlow("ar")
    val paletteId = MutableStateFlow("hiyako")
    val profileName = MutableStateFlow("alsb_tv")
    val avatarIdx = MutableStateFlow(0)
    val coverIdx = MutableStateFlow(0)
    val watchlist = MutableStateFlow<Map<String, WatchStatus>>(emptyMap())
    val history = MutableStateFlow<List<HistoryEntry>>(emptyList())
    val collections = MutableStateFlow<List<Collection>>(emptyList())
    val favChars = MutableStateFlow<Set<String>>(emptySet())
    val recents = MutableStateFlow<List<String>>(emptyList())
    val offline = MutableStateFlow<List<OfflineItem>>(emptyList())
    val notifs = MutableStateFlow(Seed.notifs)
    val chat = MutableStateFlow<List<ChatMsg>>(emptyList())
    val chatModel = MutableStateFlow("Auto")
    val spoilerCap = MutableStateFlow(Spoiler.HIGH)
    val toggles = MutableStateFlow(mapOf("continue" to true, "thumbs" to true, "notifyAll" to false, "adult" to true))
    val snack = MutableStateFlow<Snack?>(null)
    val savedArt = MutableStateFlow<Set<String>>(emptySet())

    fun init(ctx: Context) {
        prefs = ctx.getSharedPreferences("hiyako", Context.MODE_PRIVATE)
        language.value = prefs.getString("lang", "ar") ?: "ar"
        paletteId.value = prefs.getString("palette", "hiyako") ?: "hiyako"
        profileName.value = prefs.getString("name", "alsb_tv") ?: "alsb_tv"
        avatarIdx.value = prefs.getInt("avatar", 0)
        coverIdx.value = prefs.getInt("cover", 0)
        watchlist.value = prefs.getString("wl", "").orEmpty().split(";").filter { it.contains(":") }.associate {
            val p = it.split(":"); p[0] to WatchStatus.valueOf(p[1])
        }
        history.value = prefs.getString("hist", "").orEmpty().split(";").filter { it.count { c -> c == ',' } == 4 }.map {
            val p = it.split(","); HistoryEntry(p[0], p[1].toInt(), p[2].toInt(), p[3].toInt(), p[4].toLong())
        }
        collections.value = prefs.getString("cols", "").orEmpty().split(";").filter { it.contains("|") }.map {
            val p = it.split("|"); Collection(p[0], p[1], p.getOrElse(2) { "" }.split(",").filter { s -> s.isNotEmpty() })
        }
        favChars.value = prefs.getString("favc", "").orEmpty().split(",").filter { it.isNotEmpty() }.toSet()
        recents.value = prefs.getString("rec", "").orEmpty().split(",").filter { it.isNotEmpty() }
        offline.value = prefs.getString("off", "").orEmpty().split(";").filter { it.count { c -> c == ',' } == 3 }.map {
            val p = it.split(","); OfflineItem(p[0], p[1].toInt(), p[2].toInt(), p[3].toLong())
        }
        savedArt.value = prefs.getString("art", "").orEmpty().split(",").filter { it.isNotEmpty() }.toSet()
        toggles.value = toggles.value.mapValues { (k, v) -> prefs.getBoolean("tg_$k", v) }
    }

    private fun save() {
        val e = prefs.edit()
        e.putString("lang", language.value)
        e.putString("palette", paletteId.value)
        e.putString("name", profileName.value)
        e.putInt("avatar", avatarIdx.value)
        e.putInt("cover", coverIdx.value)
        e.putString("wl", watchlist.value.entries.joinToString(";") { "${it.key}:${it.value.name}" })
        e.putString("hist", history.value.joinToString(";") { "${it.animeId},${it.ep},${it.posSec},${it.durSec},${it.at}" })
        e.putString("cols", collections.value.joinToString(";") { "${it.id}|${it.name}|${it.ids.joinToString(",")}" })
        e.putString("favc", favChars.value.joinToString(","))
        e.putString("rec", recents.value.joinToString(","))
        e.putString("off", offline.value.joinToString(";") { "${it.animeId},${it.ep},${it.sizeMb},${it.at}" })
        e.putString("art", savedArt.value.joinToString(","))
        toggles.value.forEach { (k, v) -> e.putBoolean("tg_$k", v) }
        e.apply()
    }

    fun setLanguage(v: String) { language.value = v; save() }
    fun setPalette(v: String) { paletteId.value = v; save() }
    fun setName(v: String) { profileName.value = v; save() }
    fun setAvatar(v: Int) { avatarIdx.value = v; save() }
    fun setCover(v: Int) { coverIdx.value = v; save() }
    fun setToggle(k: String, v: Boolean) { toggles.value = toggles.value + (k to v); save() }

    fun setStatus(id: String, s: WatchStatus?) {
        watchlist.value = if (s == null) watchlist.value - id else watchlist.value + (id to s)
        save()
    }

    fun recordWatch(id: String, ep: Int, pos: Int, dur: Int) {
        val entry = HistoryEntry(id, ep, pos, dur, System.currentTimeMillis())
        history.value = listOf(entry) + history.value.filter { !(it.animeId == id && it.ep == ep) }
        if (pos >= dur * 95 / 100) {
            val a = Seed.anime(id)
            if (a != null && ep >= a.episodes && watchlist.value[id] != WatchStatus.COMPLETED) setStatus(id, WatchStatus.COMPLETED)
        }
        save()
    }

    fun deleteHistory(e: HistoryEntry) {
        history.value = history.value.filter { it != e }
        save()
        snack.value = Snack(deletedMsg()) { history.value = listOf(e) + history.value; save() }
    }

    fun addCollection(name: String) {
        collections.value = collections.value + Collection("c${System.currentTimeMillis()}", name, emptyList())
        save()
    }

    fun deleteCollection(c: Collection) {
        collections.value = collections.value.filter { it.id != c.id }
        save()
        snack.value = Snack(deletedMsg()) { collections.value = collections.value + c; save() }
    }

    fun toggleInCollection(colId: String, animeId: String) {
        collections.value = collections.value.map {
            if (it.id == colId) it.copy(ids = if (animeId in it.ids) it.ids - animeId else it.ids + animeId) else it
        }
        save()
    }

    fun toggleFavChar(id: String) {
        favChars.value = if (id in favChars.value) favChars.value - id else favChars.value + id
        save()
    }

    fun addRecent(q: String) {
        if (q.isBlank()) return
        recents.value = (listOf(q) + recents.value.filter { it != q }).take(12)
        save()
    }

    fun removeRecent(q: String) { recents.value = recents.value.filter { it != q }; save() }
    fun clearRecents() { recents.value = emptyList(); save() }

    fun saveOffline(id: String, ep: Int) {
        if (offline.value.any { it.animeId == id && it.ep == ep }) return
        offline.value = listOf(OfflineItem(id, ep, 192, System.currentTimeMillis())) + offline.value
        save()
        snack.value = Snack(if (language.value == "ar") "حفظ دون اتصال" else "Saved offline")
    }

    fun removeOffline(o: OfflineItem) {
        offline.value = offline.value.filter { it != o }
        save()
        snack.value = Snack(deletedMsg()) { offline.value = listOf(o) + offline.value; save() }
    }

    fun readNotif(id: String) { notifs.value = notifs.value.map { if (it.id == id) it.copy(read = true) else it } }
    fun readAll() { notifs.value = notifs.value.map { it.copy(read = true) } }
    fun clearNotifs() {
        val old = notifs.value
        notifs.value = emptyList()
        snack.value = Snack(deletedMsg()) { notifs.value = old }
    }

    fun pushChat(m: ChatMsg) { chat.value = chat.value + m }
    fun saveArt(id: String) {
        savedArt.value = savedArt.value + id
        save()
        snack.value = Snack(if (language.value == "ar") "حفظ في المعرض" else "Saved to gallery")
    }

    fun deletedMsg(): String = if (language.value == "ar") "تم الحذف" else "Deleted"
    fun clearSnack() { snack.value = null }
    fun runUndo() { snack.value?.undo?.invoke(); clearSnack() }
}
