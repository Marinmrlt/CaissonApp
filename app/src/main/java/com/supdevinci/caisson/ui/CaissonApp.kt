package com.supdevinci.caisson.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.supdevinci.caisson.ui.navigation.BottomNavItem
import com.supdevinci.caisson.ui.screens.HomeScreen
import com.supdevinci.caisson.ui.screens.LeaderboardScreen
import com.supdevinci.caisson.ui.screens.ProfileScreen
import com.supdevinci.caisson.ui.screens.QuickAddScreen

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.supdevinci.caisson.viewmodel.CocktailViewModel
import com.supdevinci.caisson.ui.screens.CocktailDetailScreen
import com.supdevinci.caisson.ui.screens.EditProfileScreen

@Composable
fun CaissonApp(viewModel: CocktailViewModel) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        bottomBar = {
            FloatingBottomBar(navController = navController)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = Color(0xFFF8F9FA) 
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { slideInHorizontally(tween(400)) { it } + fadeIn(tween(400)) },
            exitTransition = { slideOutHorizontally(tween(400)) { -it / 3 } + fadeOut(tween(400)) },
            popEnterTransition = { slideInHorizontally(tween(400)) { -it / 3 } + fadeIn(tween(400)) },
            popExitTransition = { slideOutHorizontally(tween(400)) { it } + fadeOut(tween(400)) }
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen(navController, viewModel) }
            composable(BottomNavItem.BacCalculator.route) { com.supdevinci.caisson.ui.screens.BacCalculatorScreen(navController, viewModel) }
            composable(BottomNavItem.QuickAdd.route) { QuickAddScreen(navController, viewModel, snackbarHostState) }
            composable(BottomNavItem.Profile.route) { ProfileScreen(navController, viewModel) }
            composable("analytics") { com.supdevinci.caisson.ui.screens.AnalyticsScreen(navController) }
            composable("settings") { com.supdevinci.caisson.ui.screens.SettingsScreen(navController, viewModel) }
            composable("cocktail_detail/{id}") { backStackEntry -> 
                val id = backStackEntry.arguments?.getString("id") ?: ""
                CocktailDetailScreen(navController, viewModel, id)
            }
            composable("edit_profile") { EditProfileScreen(navController, viewModel, snackbarHostState) }
        }
    }
}

@Composable
fun FloatingBottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.BacCalculator,
        BottomNavItem.QuickAdd,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Floating glassmorphism-like container
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFF161B22)) // Dark Navy background from screenshots
                .padding(vertical = 12.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                val interactionSource = remember { MutableInteractionSource() }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color(0xFFFF6D00) else Color.Transparent)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null // Remove default ripple for iOS feel
                        ) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) Color.White else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
