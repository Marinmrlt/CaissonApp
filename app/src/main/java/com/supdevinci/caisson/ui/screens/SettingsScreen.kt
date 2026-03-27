package com.supdevinci.caisson.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var biometricEnabled by remember { mutableStateOf(false) }
    var darkTheme by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text("Privacy & Security", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6D00))
            Spacer(modifier = Modifier.height(16.dp))
            ListItem(
                headlineContent = { Text("App Lock") },
                supportingContent = { Text("Require FaceID / Fingerprint to open Le Caisson") },
                trailingContent = {
                    Switch(checked = biometricEnabled, onCheckedChange = { biometricEnabled = it })
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Appearance", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6D00))
            Spacer(modifier = Modifier.height(16.dp))
            ListItem(
                headlineContent = { Text("OLED Dark Mode") },
                supportingContent = { Text("Save battery and reduce eye strain") },
                trailingContent = {
                    Switch(checked = darkTheme, onCheckedChange = { darkTheme = it })
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Data", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6D00))
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Export */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2E8F0))
            ) {
                Text("Export Data to CSV", color = Color.Black)
            }
        }
    }
}
