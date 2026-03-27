package com.supdevinci.caisson

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.supdevinci.caisson.ui.CaissonApp
import com.supdevinci.caisson.ui.theme.CaissonTheme

import androidx.fragment.app.FragmentActivity
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.supdevinci.caisson.utils.BiometricHelper
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.supdevinci.caisson.viewmodel.CocktailViewModel

class MainActivity : FragmentActivity() {
    private val viewModel: CocktailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Keep splash screen visible until the ViewModel finishes the Room API bulk sync!
        splashScreen.setKeepOnScreenCondition {
            !viewModel.isReady.value
        }

        enableEdgeToEdge()
        setContent {
            val userProfile by viewModel.userProfile.collectAsState()
            var isUnlocked by remember { mutableStateOf(false) }
            var authAttempted by remember { mutableStateOf(false) }

            CaissonTheme {
                if (userProfile.isBiometricEnabled) {
                    if (isUnlocked) {
                        CaissonApp(viewModel)
                    } else {
                        if (!authAttempted) {
                            LaunchedEffect(Unit) {
                                authAttempted = true
                                BiometricHelper.authenticate(
                                    activity = this@MainActivity,
                                    onSuccess = { isUnlocked = true },
                                    onError = { /* let user click retry */ }
                                )
                            }
                        }
                        
                        Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Filled.Lock, contentDescription="Locked app", modifier=Modifier.size(80.dp), tint=MaterialTheme.colorScheme.primary)
                                Spacer(modifier=Modifier.height(16.dp))
                                Text("App locked", style=MaterialTheme.typography.headlineMedium)
                                Spacer(modifier=Modifier.height(32.dp))
                                Button(onClick = { 
                                    BiometricHelper.authenticate(
                                        activity = this@MainActivity,
                                        onSuccess = { isUnlocked = true },
                                        onError = { }
                                    )
                                }) { Text("Unlock with Biometrics") }
                            }
                        }
                    }
                } else {
                    CaissonApp(viewModel)
                }
            }
        }
    }
}
