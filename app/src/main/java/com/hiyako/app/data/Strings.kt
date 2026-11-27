package com.hiyako.app.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

private val en = mapOf(
    "home" to "Home", "mylist" to "My List", "library" to "Library", "profile" to "Profile",
    "browse" to "Browse Anime", "search" to "Search", "settings" to "Settings", "news" to "News",
    "chat" to "SORA AI", "notifications" to "Notifications", "schedule" to "Schedule",
    "characters" to "Characters", "history" to "Watch History", "downloads" to "Downloads",
    "continue" to "Continue Watching", "recent" to "Recent Releases", "trending" to "Trending Now",
    "popular" to "Most Popular", "top" to "Highest Rated", "movies" to "Movies", "viewAll" to "View All",
    "details" to "Details", "episodes" to "Episodes", "gallery" to "Gallery", "similar" to "More Like This",
    "play" to "Play Now", "resume" to "Continue", "addList" to "Add to List", "copyOk" to "Title copied",
    "translate" to "Translate", "empty" to "Nothing here yet", "emptySub" to "Your actions will fill this space",
    "filters" to "Filters", "apply" to "Apply", "clear" to "Clear All", "sort" to "Sort by",
    "name" to "Name", "year" to "Year", "rating" to "Rating", "genres" to "Genres",
    "watching" to "Watching", "favorite" to "Favorite", "completed" to "Completed",
    "plan" to "Plan to Watch", "later" to "Finish Later", "dropped" to "Dropped",
    "collections" to "Collections", "newCollection" to "New Collection", "create" to "Create",
    "delete" to "Delete", "undo" to "Undo", "deleted" to "Deleted", "saved" to "Saved offline",
    "theme" to "Theme", "language" to "Language", "arabic" to "Arabic", "english" to "English",
    "stats" to "Activity", "episodesWatched" to "Episodes Watched", "editProfile" to "Edit Profile",
    "avatar" to "Avatar", "cover" to "Cover", "save" to "Save", "cancel" to "Cancel",
    "send" to "Send", "model" to "Model", "spoiler" to "Spoiler level", "auto" to "Auto",
    "none" to "None", "low" to "Low", "mid" to "Medium", "high" to "High",
    "askHint" to "Ask about any anime...", "readAll" to "Mark all read", "clearAll" to "Clear all",
    "week" to "This Week", "coming" to "Coming Soon", "seasons" to "Seasons", "dub" to "Dub Releases",
    "favChars" to "Favorite Characters", "allChars" to "Popular Characters", "storage" to "Storage used",
    "remove" to "Remove", "player" to "Player", "speed" to "Speed", "nextEp" to "Next episode in",
    "hours" to "h", "days" to "d", "adult" to "18+ content", "notifyAll" to "Notify all releases",
    "showContinue" to "Show Continue Watching", "thumbs" to "History thumbnails", "about" to "About",
    "version" to "Version", "clearRecents" to "Clear recent searches", "recents" to "Recent Searches",
    "results" to "results", "noResults" to "No matches found", "selected" to "Selected",
    "random" to "Pick Random Anime", "notified" to "Notified Anime", "offline" to "Offline Mode",
    "back" to "Back", "close" to "Close", "open" to "Open", "markWatched" to "Mark watched",
    "inList" to "In your list", "removeList" to "Remove from list", "story" to "Story",
    "studio" to "Studio", "country" to "Country", "type" to "Type", "status" to "Status",
    "airing" to "Airing", "finished" to "Finished", "upcomingSt" to "Upcoming", "hiatus" to "Hiatus"
)

private val ar = mapOf(
    "home" to "الرئيسية", "mylist" to "قائمتي", "library" to "المكتبة", "profile" to "حسابي",
    "browse" to "تصفح الأنمي", "search" to "بحث", "settings" to "الإعدادات", "news" to "الأخبار",
    "chat" to "SORA AI", "notifications" to "الإشعارات", "schedule" to "الجدول",
    "characters" to "الشخصيات", "history" to "سجل المشاهدة", "downloads" to "التنزيلات",
    "continue" to "متابعة المشاهدة", "recent" to "أحدث الإصدارات", "trending" to "الرائج الآن",
    "popular" to "الأكثر شعبية", "top" to "الأعلى تقييماً", "movies" to "أفلام", "viewAll" to "عرض الكل",
    "details" to "التفاصيل", "episodes" to "الحلقات", "gallery" to "المعرض", "similar" to "أعمال مشابهة",
    "play" to "شاهد الآن", "resume" to "أكمل", "addList" to "أضف للقائمة", "copyOk" to "تم نسخ الاسم",
    "translate" to "ترجمة", "empty" to "لا شيء هنا بعد", "emptySub" to "أفعالك ستملأ هذا المكان",
    "filters" to "الفلاتر", "apply" to "تطبيق", "clear" to "مسح الكل", "sort" to "ترتيب حسب",
    "name" to "الاسم", "year" to "السنة", "rating" to "التقييم", "genres" to "التصنيفات",
    "watching" to "أشاهده", "favorite" to "المفضلة", "completed" to "أكملته",
    "plan" to "أنوي مشاهدته", "later" to "أكمل لاحقاً", "dropped" to "توقف",
    "collections" to "المجموعات", "newCollection" to "مجموعة جديدة", "create" to "إنشاء",
    "delete" to "حذف", "undo" to "تراجع", "deleted" to "تم الحذف", "saved" to "حفظ دون اتصال",
    "theme" to "الثيم", "language" to "اللغة", "arabic" to "العربية", "english" to "الإنجليزية",
    "stats" to "النشاط", "episodesWatched" to "حلقات شوهدت", "editProfile" to "تعديل الحساب",
    "avatar" to "الأفاتار", "cover" to "الغلاف", "save" to "حفظ", "cancel" to "إلغاء",
    "send" to "إرسال", "model" to "النموذج", "spoiler" to "مستوى الحرق", "auto" to "تلقائي",
    "none" to "بدون", "low" to "بسيط", "mid" to "متوسط", "high" to "كبير",
    "askHint" to "اسأل عن أي أنمي...", "readAll" to "تحديد الكل مقروء", "clearAll" to "مسح الكل",
    "week" to "هذا الأسبوع", "coming" to "قريباً", "seasons" to "المواسم", "dub" to "إصدارات مدبلجة",
    "favChars" to "الشخصيات المفضلة", "allChars" to "الشخصيات الرائجة", "storage" to "المساحة المستخدمة",
    "remove" to "إزالة", "player" to "المشغل", "speed" to "السرعة", "nextEp" to "الحلقة القادمة بعد",
    "hours" to "س", "days" to "ي", "adult" to "محتوى +18", "notifyAll" to "إشعارات كل الإصدارات",
    "showContinue" to "إظهار متابعة المشاهدة", "thumbs" to "مصغرات السجل", "about" to "حول",
    "version" to "الإصدار", "clearRecents" to "مسح سجل البحث", "recents" to "عمليات البحث الأخيرة",
    "results" to "نتيجة", "noResults" to "لا توجد نتائج مطابقة", "selected" to "المحدد",
    "random" to "أنمي عشوائي", "notified" to "أنمي مشعر", "offline" to "وضع دون اتصال",
    "back" to "رجوع", "close" to "إغلاق", "open" to "فتح", "markWatched" to "تحديد كمشاهد",
    "inList" to "في قائمتك", "removeList" to "إزالة من القائمة", "story" to "القصة",
    "studio" to "الاستوديو", "country" to "البلد", "type" to "النوع", "status" to "الحالة",
    "airing" to "مستمر", "finished" to "منتهي", "upcomingSt" to "قادم", "hiatus" to "متوقف"
)

@Composable
fun t(key: String): String {
    val lang by Store.language.collectAsState()
    val dict = if (lang == "ar") ar else en
    return dict[key] ?: en[key] ?: key
}
