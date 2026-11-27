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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.hiyako.app.ui.navigation.HiyakoNavHost
import com.hiyako.app.ui.navigation.Routes
import com.hiyako.app.ui.theme.AccentRed
import com.hiyako.app.ui.theme.DarkBg
import com.hiyako.app.ui.theme.DrawerBg
import com.hiyako.app.ui.theme.TextPrimary
import com.hiyako.app.ui.theme.TextSecondary
import kotlinx.coroutines.launch

private data class DrawerItem(val label: String, val icon: ImageVector)

private val drawerItems = listOf(
    DrawerItem("Home", Icons.Filled.Home),
    DrawerItem("Notifications", Icons.Filled.Notifications),
    DrawerItem("Search", Icons.Filled.Search),
    DrawerItem("Pick Random Anime", Icons.Outlined.Shuffle),
    DrawerItem("News", Icons.Outlined.Article),
    DrawerItem("SORA AI", Icons.Outlined.SmartToy),
    DrawerItem("Recent Releases", Icons.Outlined.PlayCircle),
    DrawerItem("Recent Dub Releases", Icons.Outlined.Mic),
    DrawerItem("Trending Anime", Icons.Outlined.TrendingUp),
    DrawerItem("Schedule", Icons.Outlined.DateRange),
    DrawerItem("Coming Soon", Icons.Outlined.Upcoming),
    DrawerItem("Library", Icons.Outlined.BookmarkBorder),
    DrawerItem("Seasons", Icons.Outlined.CalendarMonth),
    DrawerItem("Popular Characters", Icons.Outlined.People),
    DrawerItem("Favorite Characters", Icons.Outlined.FavoriteBorder),
    DrawerItem("Downloads/Offline", Icons.Outlined.Download),
    DrawerItem("Watch History", Icons.Outlined.History),
    DrawerItem("My List", Icons.Outlined.List),
    DrawerItem("Collections", Icons.Outlined.Folder),
    DrawerItem("Notified Anime", Icons.Outlined.NotificationsActive),
    DrawerItem("Settings", Icons.Filled.Settings)
)

private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(Routes.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Routes.HOME

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = DrawerBg, modifier = Modifier.width(300.dp)) {
                Box(modifier = Modifier.height(200.dp).fillMaxWidth()) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1495616811223-4d98c6e9c869?auto=format&fit=crop&w=600&q=80",
                        contentDescription = "Cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.align(Alignment.TopEnd).padding(16.dp).size(32.dp).background(Color.Black.copy(alpha = 0.5f), CircleShape).clickable { scope.launch { drawerState.close() } },
                        contentAlignment = Alignment.Center
                    ) { Text("X", color = Color.White, fontSize = 16.sp) }
                    
                    Box(modifier = Modifier.align(Alignment.BottomStart).padding(start = 16.dp, bottom = 16.dp)) {
                        AsyncImage(
                            model = "https://s4.anilist.co/file/anilistcdn/character/large/b1-1yly1.jpg",
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(80.dp).clip(CircleShape).background(Color.White)
                        )
                        Box(
                            modifier = Modifier.align(Alignment.BottomEnd).size(24.dp).background(Color(0xFFFF9800), CircleShape),
                            contentAlignment = Alignment.Center
                        ) { Text("✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    }
                    Column(modifier = Modifier.align(Alignment.BottomStart).padding(start = 112.dp, bottom = 28.dp)) {
                        Text("alsb_tv", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("@alsb_tv", color = TextSecondary, fontSize = 14.sp)
                    }
                }
                LazyColumn {
                    items(drawerItems) { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable { scope.launch { drawerState.close() } }.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(item.icon, contentDescription = item.label, tint = TextPrimary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(item.label, color = TextPrimary, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            containerColor = DarkBg,
            bottomBar = {
                NavigationBar(containerColor = DrawerBg, modifier = Modifier.height(64.dp)) {
                    NavigationBarItem(
                        selected = currentRoute == Routes.HOME,
                        onClick = { navController.navigateToTab(Routes.HOME) },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Home", tint = if (currentRoute == Routes.HOME) AccentRed else TextSecondary) },
                        label = { Text("Home", color = if (currentRoute == Routes.HOME) AccentRed else TextSecondary, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.LIBRARY,
                        onClick = { navController.navigateToTab(Routes.LIBRARY) },
                        icon = { Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Library", tint = if (currentRoute == Routes.LIBRARY) AccentRed else TextSecondary) },
                        label = { Text("Library", color = if (currentRoute == Routes.LIBRARY) AccentRed else TextSecondary, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.PROFILE,
                        onClick = { navController.navigateToTab(Routes.PROFILE) },
                        icon = { AsyncImage(model = "https://s4.anilist.co/file/anilistcdn/character/large/b1-1yly1.jpg", contentDescription = "Profile", contentScale = ContentScale.Crop, modifier = Modifier.size(24.dp).clip(CircleShape)) },
                        label = { Text("Profile", color = if (currentRoute == Routes.PROFILE) AccentRed else TextSecondary, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                HiyakoNavHost(navController = navController, onMenuClick = { scope.launch { drawerState.open() } })
            }
        }
    }
}
