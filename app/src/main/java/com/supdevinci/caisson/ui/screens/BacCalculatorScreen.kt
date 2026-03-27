package com.supdevinci.caisson.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.supdevinci.caisson.viewmodel.CocktailViewModel

@Composable
fun BacCalculatorScreen(navController: NavController, viewModel: CocktailViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    QuickAddScreen(
        navController = navController,
        viewModel = viewModel,
        snackbarHostState = snackbarHostState
    )
}
