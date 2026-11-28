package com.hiyako.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hiyako.app.data.Seed
import com.hiyako.app.data.Snack
import com.hiyako.app.data.Store
import com.hiyako.app.ui.components.AppIcons
import com.hiyako.app.ui.components.AvatarCircle
import com.hiyako.app.ui.components.CoverArt
import com.hiyako.app.ui.components.isArabic
import com.hiyako.app.ui.navigation.HiyakoNavHost
import com.hiyako.app.ui.navigation.Routes
import com.hiyako.app.ui.navigation.navigateToTab
import com.hiyako.app.ui.theme.LocalPalette
import kotlinx.coroutines.launch

private enum class Target { HOME, MYLIST, LIBRARY, PROFILE, RANDOM, SOON }

private data class DrawerItem(val ar: String, val en: String, val icon: ImageVector, val target: Target)

private val drawerItems = listOf(
    DrawerItem("الرئيسية", "Home", AppIcons.home, Target.HOME),
    DrawerItem("الإشعارات", "Notifications", AppIcons.bell, Target.SOON),
    DrawerItem("بحث", "Search", AppIcons.search, Target.LIBRARY),
    DrawerItem("أنمي عشوائي", "Pick Random Anime", AppIcons.shuffle, Target.RANDOM),
    DrawerItem("الأخبار", "News", AppIcons.news, Target.SOON),
    DrawerItem("SORA AI", "SORA AI", AppIcons.ai, Target.SOON),
    DrawerItem("أحدث الإصدارات", "Recent Releases", AppIcons.play, Target.HOME),
    DrawerItem("إصدارات مدبلجة", "Recent Dub Releases", AppIcons.mic, Target.HOME),
    DrawerItem("الأنمي الرائج", "Trending Anime", AppIcons.trend, Target.HOME),
    DrawerItem("الجدول", "Schedule", AppIcons.calendar, Target.SOON),
    DrawerItem("قريباً", "Coming Soon", AppIcons.upcoming, Target.SOON),
    DrawerItem("المكتبة", "Library", AppIcons.library, Target.LIBRARY),
    DrawerItem("المواسم", "Seasons", AppIcons.calendar, Target.SOON),
    DrawerItem("الشخصيات الرائجة", "Popular Characters", AppIcons.people, Target.SOON),
    DrawerItem("الشخصيات المفضلة", "Favorite Characters", AppIcons.heart, Target.SOON),
    DrawerItem("التنزيلات / دون اتصال", "Downloads/Offline", AppIcons.download, Target.SOON),
    DrawerItem("سجل المشاهدة", "Watch History", AppIcons.history, Target.PROFILE),
    DrawerItem("قائمتي", "My List", AppIcons.list, Target.MYLIST),
    DrawerItem("المجموعات", "Collections", AppIcons.folder, Target.MYLIST),
    DrawerItem("أنمي مشعر", "Notified Anime", AppIcons.bellActive, Target.SOON),
    DrawerItem("الإعدادات", "Settings", AppIcons.settings, Target.PROFILE)
)

private data class BarItem(val route: String, val ar: String, val en: String, val icon: ImageVector)

private val barItems = listOf(
    BarItem(Routes.HOME, "الرئيسية", "Home", AppIcons.home),
    BarItem(Routes.MYLIST, "قائمتي", "My List", AppIcons.myList),
    BarItem(Routes.LIBRARY, "المكتبة", "Library", AppIcons.library),
    BarItem(Routes.PROFILE, "حسابي", "Profile", AppIcons.profile)
)

@Composable
fun MainScreen() {
    val p = LocalPalette.current
    val ar = isArabic()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Routes.HOME
    val snackHost = remember { SnackbarHostState() }
    val snack by Store.snack.collectAsState()
    val name by Store.profileName.collectAsState()
    val avatarIdx by Store.avatarIdx.collectAsState()
    val coverIdx by Store.coverIdx.collectAsState()
    val showBar = currentRoute != "details/{id}" && !currentRoute.startsWith("details/")

    LaunchedEffect(snack) {
        val s = snack
        if (s != null) {
            val res = snackHost.showSnackbar(
                message = s.msg,
                actionLabel = if (s.undo != null) (if (ar) "تراجع" else "Undo") else null,
                duration = SnackbarDuration.Short
            )
            if (res == SnackbarResult.ActionPerformed) Store.runUndo() else Store.clearSnack()
        }
    }

    val soonMsg = if (ar) "هذه الشاشة قادمة في مرحلة لاحقة" else "This screen arrives in a later phase"

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = p.drawer, modifier = Modifier.width(300.dp)) {
                Box(Modifier.height(190.dp).fillMaxWidth()) {
                    CoverArt(coverIdx, Modifier.fillMaxSize())
                    Box(
                        Modifier.align(Alignment.TopEnd).padding(14.dp).size(32.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .clip(CircleShape)
                            .clickable { scope.launch { drawerState.close() } },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(AppIcons.close, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Row(
                        Modifier.align(Alignment.BottomStart).padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AvatarCircle(avatarIdx, 72.dp)
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text(name, color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                            Text("@$name", color = Color.White.copy(alpha = 0.75f), fontSize = 13.sp)
                        }
                    }
                }
                LazyColumn {
                    items(drawerItems) { item ->
                        Row(
                            Modifier.fillMaxWidth()
                                .clickable {
                                    scope.launch { drawerState.close() }
                                    when (item.target) {
                                        Target.HOME -> navController.navigateToTab(Routes.HOME)
                                        Target.MYLIST -> navController.navigateToTab(Routes.MYLIST)
                                        Target.LIBRARY -> navController.navigateToTab(Routes.LIBRARY)
                                        Target.PROFILE -> navController.navigateToTab(Routes.PROFILE)
                                        Target.RANDOM -> {
                                            val pick = Seed.anime.random()
                                            navController.navigate(Routes.details(pick.id))
                                        }
                                        Target.SOON -> Store.snack.value = Snack(soonMsg)
                                    }
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(item.icon, null, tint = p.text, modifier = Modifier.size(22.dp))
                            Spacer(Modifier.width(16.dp))
                            Text(if (ar) item.ar else item.en, color = p.text, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            containerColor = p.bg,
            snackbarHost = { SnackbarHost(snackHost) },
            bottomBar = {
                if (showBar) {
                    NavigationBar(containerColor = p.drawer, modifier = Modifier.height(68.dp)) {
                        barItems.forEach { item ->
                            val sel = currentRoute == item.route
                            NavigationBarItem(
                                selected = sel,
                                onClick = { navController.navigateToTab(item.route) },
                                icon = {
                                    Icon(item.icon, null, tint = if (sel) p.accent else p.textDim, modifier = Modifier.size(24.dp))
                                },
                                label = {
                                    Text(
                                        if (ar) item.ar else item.en,
                                        color = if (sel) p.accent else p.textDim,
                                        fontSize = 10.sp,
                                        fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                            )
                        }
                    }
                }
            }
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                HiyakoNavHost(navController = navController, onMenuClick = { scope.launch { drawerState.open() } })
            }
        }
    }
}
