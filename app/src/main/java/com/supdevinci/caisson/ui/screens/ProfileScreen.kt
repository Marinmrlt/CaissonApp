package com.supdevinci.caisson.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.supdevinci.caisson.viewmodel.CocktailViewModel

@Composable
fun ProfileScreen(navController: NavController, viewModel: CocktailViewModel) {
    val profile by viewModel.userProfile.collectAsState()
    val recentDrinks by viewModel.recentDrinks.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)) // Uniform Light Background
            .padding(top = 32.dp, start = 24.dp, end = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // User Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(32.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(32.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=200&auto=format&fit=crop",
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(profile.name, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                        Text("${profile.gender} • ${profile.age}y • ${profile.weightKg}kg", color = Color.Gray, fontSize = 14.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Explicit Action Button
                Button(
                    onClick = { navController.navigate("edit_profile") },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit Profile", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("EDIT BIOMETRICS", fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 1.sp)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        // Stats Grid
        Text("Your Mixology Stats", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Stat 1
            Card(
                modifier = Modifier.weight(1f).height(120.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2FE)), // Light Blue variant
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Filled.LocalBar, contentDescription = "Drinks", tint = Color(0xFF0284C7))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(recentDrinks.size.toString(), fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFF0284C7))
                    Text("24h Drinks", color = Color(0xFF0369A1), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
            // Stat 2
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp)
                    .clickable { navController.navigate("leaderboard") },
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD1FAE5)), // Light Emerald variant
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Filled.TrendingUp, contentDescription = "Rank", tint = Color(0xFF059669))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("#42", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFF059669))
                    Text("Global Rank", color = Color(0xFF047857), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE9FE)), // Light Violet variant
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, contentDescription = "Favorite", tint = Color(0xFF7C3AED), modifier = Modifier.size(40.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Top Cocktail", color = Color(0xFF6D28D9), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Text("Mojito", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color(0xFF7C3AED))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Settings Entry
        Button(
            onClick = { navController.navigate("settings") },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            elevation = ButtonDefaults.buttonElevation(4.dp)
        ) {
            Text("APPLICATION SETTINGS", color = Color(0xFF0F172A), fontWeight = FontWeight.ExtraBold)
        }

        Spacer(modifier = Modifier.height(120.dp))
    }
}
