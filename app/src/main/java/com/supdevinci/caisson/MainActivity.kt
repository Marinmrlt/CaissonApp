package com.supdevinci.caisson

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.supdevinci.caisson.ui.CaissonApp
import com.supdevinci.caisson.ui.theme.CaissonTheme

import androidx.activity.viewModels
import com.supdevinci.caisson.viewmodel.CocktailViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: CocktailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaissonTheme {
                CaissonApp(viewModel)
            }
        }
    }
}
