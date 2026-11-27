package com.hiyako.app.data

object Seed {
    val anime = listOf(
        Anime("frieren", "Sousou no Frieren", "葬送のフリーレン", "Frieren: Beyond Journey's End", 2023, "Fall", "TV", "Completed", 9.25, 9.34, 941000, 256000, 28, listOf("Adventure", "Drama", "Fantasy"), "The elf mage Frieren outlives the hero party and journeys north to understand the humans she once traveled with.", "الساحرة إلف فرييرين تعمّر بعد رفاقها البشريين، فترحل شمالاً لتفهم البشر الذين سافرت معهم يوماً.", "Madhouse", "Japan", 0xFF2E7D5B, 0xFF0F3D3E, "葬送", 5, 0, false, true),
        Anime("jjk", "Jujutsu Kaisen", "呪術廻戦", "Sorcery Fight", 2020, "Fall", "TV", "Completed", 8.58, 8.6, 306000, 240000, 24, listOf("Action", "Supernatural", "School"), "Yuji Itadori swallows a cursed finger and enters the world of jujutsu sorcerers to fight curses.", "يبتلع يوجي إصبعاً ملعوناً ويدخل عالم سحرة الجوجوتسو لمحاربة اللعنات.", "MAPPA", "Japan", 0xFFB3261E, 0xFF3E1010, "呪術", 6, 0, true, true),
        Anime("onepiece", "One Piece", "ワンピース", "One Piece", 1999, "Fall", "TV", "On Going", 8.72, 8.7, 1200000, 256000, 1122, listOf("Action", "Adventure", "Comedy"), "Monkey D. Luffy sails the Grand Line with his crew to find the One Piece and become King of the Pirates.", "لوفي يبحر في الغراند لاين مع طاقمه للعثور على ون بيس وليصبح ملك القراصنة.", "Toei", "Japan", 0xFFE65100, 0xFF7A1F00, "海賊", 0, 18, false, true),
        Anime("aot", "Shingeki no Kyojin", "進撃の巨人", "Attack on Titan", 2013, "Spring", "TV", "Completed", 8.57, 8.5, 3100000, 300000, 25, listOf("Action", "Drama", "Mystery"), "Humanity fights man-eating titans from behind walls, until Eren Yeager vows to exterminate them all.", "البشرية تقاتل العمالقة آكلي البشر خلف الأسوار، حتى يقسم إيرن على إبادتهم جميعاً.", "Wit / MAPPA", "Japan", 0xFF5D4037, 0xFF20120B, "進撃", 6, 0, true, true),
        Anime("deathnote", "Death Note", "デスノート", "Death Note", 2006, "Fall", "TV", "Completed", 8.62, 8.6, 3000000, 280000, 37, listOf("Mystery", "Psychological", "Supernatural"), "A notebook that kills anyone whose name is written in it falls into the hands of genius Light Yagami.", "دفتر يقتل كل من يكتب اسمه فيه يقع بين يدي العبقري لايت ياغامي.", "Madhouse", "Japan", 0xFF37474F, 0xFF0B0F12, "死神", 2, 0, true, true),
        Anime("fmab", "Fullmetal Alchemist: Brotherhood", "鋼の錬金術師", "Fullmetal Alchemist: Brotherhood", 2009, "Spring", "TV", "Completed", 9.11, 9.1, 2300000, 243000, 64, listOf("Action", "Adventure", "Drama"), "Two brothers pay a terrible price for forbidden alchemy and search for the Philosopher's Stone.", "أخوان يدفعان ثمناً فادحاً مقابل خيمياء محرمة ويبحثان عن حجر الفلاسفة.", "Bones", "Japan", 0xFFC62828, 0xFF4A0E0E, "鋼", 3, 0, false, true),
        Anime("hxh", "Hunter x Hunter", "ハンター×ハンター", "Hunter x Hunter", 2011, "Fall", "TV", "Completed", 9.04, 9.0, 1800000, 230000, 148, listOf("Action", "Adventure", "Fantasy"), "Gon Freecss becomes a Hunter to find the father who left him, facing the Chimera Ant threat.", "غون يصبح هنتر ليجد أباه الذي تركه، ويواجه خطر نمل الكيميرا.", "Madhouse", "Japan", 0xFF2E7D32, 0xFF0C2B12, "狩", 4, 0, false, true),
        Anime("spyfamily", "Spy x Family", "スパイファミリー", "Spy x Family", 2022, "Spring", "TV", "Completed", 8.44, 8.4, 900000, 200000, 25, listOf("Comedy", "Action", "Slice of Life"), "A spy, an assassin and a telepath child form a fake family, each hiding their true identity.", "جاسوس وقاتلة وطفلة قارئة أفكار يشكلون عائلة مزيفة، كل يخفي هويته الحقيقية.", "Wit / CloverWorks", "Japan", 0xFFAD1457, 0xFF33061B, "家族", 6, 0, false, true),
        Anime("csm", "Chainsaw Man", "チェンソーマン", "Chainsaw Man", 2022, "Fall", "TV", "Completed", 8.47, 8.5, 1100000, 220000, 12, listOf("Action", "Horror", "Supernatural"), "Denji merges with his devil dog Pochita and becomes Chainsaw Man, hunting devils for a normal life.", "دينجي يندمج مع كلبه الشيطاني بوتشيتا ويصبح رجل المنشار، يصطاد الشياطين لحياة طبيعية.", "MAPPA", "Japan", 0xFFD84315, 0xFF3B0D02, "锯", 2, 0, true, true),
        Anime("demonslayer", "Kimetsu no Yaiba", "鬼滅の刃", "Demon Slayer", 2019, "Spring", "TV", "Completed", 8.45, 8.5, 1500000, 230000, 26, listOf("Action", "Supernatural", "Historical"), "Tanjiro joins the Demon Slayer Corps to cure his sister Nezuko, turned into a demon.", "تانجيرو ينضم لفيلق قتلة الشياطين ليشفي أخته نيزوكو التي تحولت لشيطانة.", "ufotable", "Japan", 0xFF00695C, 0xFF03211D, "鬼滅", 0, 0, false, true),
        Anime("vinland", "Vinland Saga", "ヴィンランド・サガ", "Vinland Saga", 2019, "Summer", "TV", "Completed", 8.76, 8.8, 800000, 180000, 24, listOf("Action", "Adventure", "Drama"), "Thorfinn grows up among Vikings seeking revenge, then searches for a land without war.", "ثورفين يكبر بين الفايكنغ ساعياً للانتقام، ثم يبحث عن أرض بلا حرب.", "Wit / MAPPA", "Japan", 0xFF455A64, 0xFF101820, "氷", 1, 0, true, true),
        Anime("bocchi", "Bocchi the Rock!", "ぼっち・ざ・ろっく!", "Bocchi the Rock!", 2022, "Fall", "TV", "Completed", 8.79, 8.8, 400000, 150000, 12, listOf("Comedy", "Music", "Slice of Life"), "Hitori Gotoh, a painfully shy guitarist, joins Kessoku Band and learns to shine on stage.", "هيتوري غوتو، عازفة غيتار خجولة جداً، تنضم لفريق كيسوكو وتتعلم التألق على المسرح.", "CloverWorks", "Japan", 0xFFEC407A, 0xFF4A0D24, "ぼっち", 6, 0, false, true),
        Anime("bluelock", "Blue Lock", "ブルーロック", "Blue Lock", 2022, "Fall", "TV", "Completed", 8.14, 8.2, 600000, 160000, 24, listOf("Sports", "Drama", "Psychological"), "300 strikers are locked in a facility where only the most egoistic will become Japan's striker.", "ثلاثمئة مهاجم يحبسون في مرفق حيث الأكثر أنانية فقط يصبح مهاجم اليابان.", "8bit", "Japan", 0xFF1565C0, 0xFF04142B, "青", 6, 0, false, true),
        Anime("apothecary", "Kusuriya no Hitorigoto", "薬屋のひとりごと", "The Apothecary Diaries", 2023, "Fall", "TV", "On Going", 8.6, 8.6, 210000, 140000, 24, listOf("Mystery", "Drama", "Historical"), "Maomao, an apothecary maid in the imperial court, solves poison mysteries with sharp curiosity.", "ماوماو، خادمة صيدلانية في البلاط الإمبراطوري، تحل ألغاز السموم بفضول حاد.", "Toho / OLM", "Japan", 0xFF6A1B9A, 0xFF1D0726, "薬", 5, 26, false, true),
        Anime("sololeveling", "Ore dake Level Up na Ken", "俺だけレベルアップな件", "Solo Leveling", 2024, "Winter", "TV", "On Going", 8.25, 8.3, 500000, 170000, 12, listOf("Action", "Fantasy", "Adventure"), "The weakest hunter Sung Jinwoo gains a system that lets him level up without limit.", "أضعف هنتر سونغ جينوو يحصل على نظام يتيح له رفع مستواه بلا حدود.", "A-1 Pictures", "Korea", 0xFF4527A0, 0xFF120826, "影", 6, 30, false, true),
        Anime("cyberpunk", "Cyberpunk: Edgerunners", "サイバーパンク エッジランナーズ", "Cyberpunk: Edgerunners", 2022, "Summer", "ONA", "Completed", 8.38, 8.6, 13000, 120000, 10, listOf("Action", "Sci-Fi", "Drama"), "A street kid in Night City becomes an edgerunner mercenary with chrome implants and nothing to lose.", "فتى شوارع في نايت سيتي يصبح مرتزق إيدجرانر بزرعات كروم ولا شيء يخسره.", "Trigger", "Japan", 0xFFF9A825, 0xFF2B1600, "夜", 3, 0, true, true)
    )

    val news = listOf(
        NewsItem("n1", "الإعلان عن موسم ثانٍ لـ Solo Leveling", "Solo Leveling Season 2 announced", "أكد الاستوديو عودة سونغ جينوو في موسم ثانٍ يغطي آرك جزيرة جيجو، مع عرض تشويقي جديد.", "The studio confirmed Sung Jinwoo's return in a second season covering the Jeju Island arc, with a new teaser.", "AniList News", 3, 0xFF4527A0, 0xFF120826),
        NewsItem("n2", "فرييرين يتصدر استفتاء أفضل أنمي للسنة", "Frieren tops anime of the year poll", "تصدرت Sousou no Frieren استفتاء النقاد بفارق واسع عن المنافسين بفضل إخراجها الهادئ.", "Sousou no Frieren topped the critics poll by a wide margin thanks to its quiet direction.", "MAL News", 9, 0xFF2E7D5B, 0xFF0F3D3E),
        NewsItem("n3", "ون بيس يدخل آرك إلباف رسمياً", "One Piece enters Elbaf arc", "الفصل الجديد ينقل الطاقم إلى أرض العمالقة إلباف بعد أحداث إيغهيد.", "The new chapter moves the crew to the giants' land Elbaf after Egghead events.", "Shonen Jump", 21, 0xFFE65100, 0xFF7A1F00),
        NewsItem("n4", "فيلم Chainsaw Man: Reze Arc يحطم شباك التذاكر", "Chainsaw Man Reze Arc film breaks box office", "حقق الفيلم أعلى افتتاحية لسلسلة أفلام الأنمي هذا الموسم في اليابان.", "The film posted the highest opening for an anime film this season in Japan.", "ANN", 30, 0xFFD84315, 0xFF3B0D02),
        NewsItem("n5", "أزرق السجن: مباراة اليابان تحت 20 تقترب", "Blue Lock U-20 match approaches", "الاستوديو يكشف عن تصميمات الشخصيات الجديدة لمباراة المنتخب.", "The studio revealed new character designs for the national team match.", "AniList News", 44, 0xFF1565C0, 0xFF04142B),
        NewsItem("n6", "مذكرات الصيدلانية تجدد لموسم ثالث", "Apothecary Diaries renewed for season 3", "إعلان رسمي بموسم ثالث يغطي آرك العاصمة الشمالية.", "Official announcement of a third season covering the northern capital arc.", "MAL News", 60, 0xFF6A1B9A, 0xFF1D0726)
    )

    val characters = listOf(
        Character("c1", "Frieren", "フリーレン", "frieren", 98000, 0xFF2E7D5B, 0xFF0F3D3E),
        Character("c2", "Gojo Satoru", "五条悟", "jjk", 150000, 0xFFB3261E, 0xFF3E1010),
        Character("c3", "Luffy", "ルフィ", "onepiece", 200000, 0xFFE65100, 0xFF7A1F00),
        Character("c4", "Levi", "リヴァイ", "aot", 180000, 0xFF5D4037, 0xFF20120B),
        Character("c5", "L Lawliet", "エル", "deathnote", 160000, 0xFF37474F, 0xFF0B0F12),
        Character("c6", "Edward Elric", "エドワード", "fmab", 120000, 0xFFC62828, 0xFF4A0E0E),
        Character("c7", "Killua", "キルア", "hxh", 140000, 0xFF2E7D32, 0xFF0C2B12),
        Character("c8", "Anya Forger", "アーニャ", "spyfamily", 130000, 0xFFAD1457, 0xFF33061B),
        Character("c9", "Power", "パワー", "csm", 90000, 0xFFD84315, 0xFF3B0D02),
        Character("c10", "Nezuko", "禰豆子", "demonslayer", 110000, 0xFF00695C, 0xFF03211D),
        Character("c11", "Maomao", "猫猫", "apothecary", 85000, 0xFF6A1B9A, 0xFF1D0726),
        Character("c12", "Hitori Gotoh", "後藤ひとり", "bocchi", 95000, 0xFFEC407A, 0xFF4A0D24)
    )

    val notifs = listOf(
        Notif("t1", "حلقة جديدة من One Piece متاحة الآن", "New One Piece episode available now", 2, false, "onepiece"),
        Notif("t2", "موسم جديد من Apothecary Diaries أعلن", "New Apothecary Diaries season announced", 5, false, "apothecary"),
        Notif("t3", "خبر عاجل: فيلم Chainsaw Man في السينما", "Breaking: Chainsaw Man film in theaters", 12, false, "csm"),
        Notif("t4", "حلقة Solo Leveling القادمة بعد 30 ساعة", "Solo Leveling next episode in 30h", 20, true, "sololeveling"),
        Notif("t5", "تحديث تقييم Frieren على MAL", "Frieren MAL rating updated", 40, true, "frieren")
    )

    fun anime(id: String): Anime? = anime.firstOrNull { it.id == id }
}
