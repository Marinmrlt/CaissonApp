package com.supdevinci.caisson.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Filled.Home, "Home")
    object Leaderboard : BottomNavItem("leaderboard", Icons.Filled.EmojiEvents, "Leaderboard")
    object QuickAdd : BottomNavItem("quick_add", Icons.Filled.Add, "Tracker")
    object Profile : BottomNavItem("profile", Icons.Filled.Person, "Profile")
}
