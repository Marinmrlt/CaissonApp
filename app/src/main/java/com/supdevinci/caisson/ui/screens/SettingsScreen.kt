package com.supdevinci.caisson.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.supdevinci.caisson.viewmodel.CocktailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: CocktailViewModel) {
    val profile by viewModel.userProfile.collectAsState()

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text("Security", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Biometric Toggle Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp)
                    .shadow(4.dp, RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = Color(0xFFE2E8F0)) {
                        Icon(Icons.Filled.Fingerprint, contentDescription = "Security", modifier = Modifier.padding(12.dp), tint = Color(0xFF0F172A))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Biometric Lock", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF0F172A))
                        Text(if (profile.isBiometricEnabled) "Enabled securely" else "App is unprotected", color = Color.Gray, fontSize = 12.sp)
                    }
                    Switch(
                        checked = profile.isBiometricEnabled,
                        onCheckedChange = { isEnabled ->
                            viewModel.updateUserProfile(
                                name = profile.name,
                                gender = profile.gender,
                                age = profile.age,
                                weightKg = profile.weightKg,
                                isBiometricEnabled = isEnabled
                            )
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFFF6D00))
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("Data Management", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Export Data Button
            Button(
                onClick = { /* Implement real CSV export here later */ },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(Icons.Filled.FileDownload, contentDescription = "Export", tint = Color.White)
                Spacer(modifier = Modifier.width(12.dp))
                Text("EXPORT BAC DATA TO CSV", fontWeight = FontWeight.Black, letterSpacing = 1.sp, color = Color.White)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Delete Account
            Button(
                onClick = { viewModel.clearAllHistory() },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEF2F2)),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(Icons.Filled.DeleteOutline, contentDescription = "Delete", tint = Color(0xFFDC2626))
                Spacer(modifier = Modifier.width(12.dp))
                Text("CLEAR ALL HISTORY", fontWeight = FontWeight.Black, letterSpacing = 1.sp, color = Color(0xFFDC2626))
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Branding
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Filled.SettingsSystemDaydream, contentDescription="Logo", tint=Color.LightGray, modifier=Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Le Caisson v2.0.0", color = Color.Gray, fontWeight = FontWeight.Bold)
                Text("State of the Art Engine", color = Color.LightGray, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
