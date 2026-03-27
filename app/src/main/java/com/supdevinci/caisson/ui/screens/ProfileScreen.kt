package com.supdevinci.caisson.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(top = 32.dp, start = 24.dp, end = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // User Header
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = "https://i.pravatar.cc/150?u=johndoe",
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(profile.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("${profile.gender} • ${profile.age} years • ${profile.weightKg}kg", color = Color.Gray, fontSize = 14.sp)
            }
            IconButton(onClick = { navController.navigate("edit_profile") }) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit Profile", tint = Color.Gray)
            }
        }

        
        Spacer(modifier = Modifier.height(32.dp))

        // Stats Row
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Total Drinks
            Card(
                modifier = Modifier.weight(1f).height(100.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Drinks", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text("5", fontSize = 32.sp, fontWeight = FontWeight.Light)
                }
            }
            // Avg per Day
            Card(
                modifier = Modifier.weight(1f).height(100.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.DateRange, contentDescription = "Calendar", tint = Color(0xFF3B82F6), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Avg per Day", color = Color.Gray, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text("1.3", fontSize = 32.sp, fontWeight = FontWeight.Light)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Fun Fact Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)), // Very light orange
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Fun Fact!", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "The alcohol you've consumed this week could power a candle for ",
                    color = Color.DarkGray, fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("40.2 hours", color = Color(0xFFFF6D00), fontWeight = FontWeight.Bold)
                Text("! That's enough light to read about 3 novels.", color = Color.DarkGray, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Drinking Insights
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Drinking Insights", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Most active day:", color = Color.Gray, fontSize = 14.sp)
                    Text("24/03/2026", fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Regional rank:", color = Color.Gray, fontSize = 14.sp)
                    Text("#1", fontSize = 14.sp, color = Color(0xFFFF6D00), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Days tracked:", color = Color.Gray, fontSize = 14.sp)
                    Text("4", fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("Recent History", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // History items
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Mojito", fontWeight = FontWeight.Bold)
                    Text("24/03/2026 at 20:30:00", color = Color.Gray, fontSize = 12.sp)
                }
                Text("13% ABV", color = Color(0xFFFF6D00), fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Advanced Screens Navigation
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { navController.navigate("analytics") },
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("In-Depth Analytics")
            }
            Button(
                onClick = { navController.navigate("settings") },
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("App Settings")
            }
        }

        Spacer(modifier = Modifier.height(100.dp)) // Nav pad
    }
}
