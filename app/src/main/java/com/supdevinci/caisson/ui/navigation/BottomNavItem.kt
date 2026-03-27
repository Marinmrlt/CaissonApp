package com.supdevinci.caisson.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

// We use an extended icon set for EmojiEvents (Trophy) but we can use a placeholder if it's not available.
// For now, let's use standard icons.
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Filled.Home, "Home")
    object BacCalculator : BottomNavItem("bac_calculator", Icons.Filled.LocalBar, "BAC")
    object QuickAdd : BottomNavItem("quick_add", Icons.Filled.Add, "Tracker")
    object Profile : BottomNavItem("profile", Icons.Filled.Person, "Profile")
}
