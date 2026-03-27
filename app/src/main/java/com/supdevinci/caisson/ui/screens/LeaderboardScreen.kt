package com.supdevinci.caisson.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun LeaderboardScreen(navController: NavController) {
    val tabs = listOf("World", "National", "Regional")
    var selectedTab by remember { mutableStateOf("World") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)) // Very light gray/off-white background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Leaderboard",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Top drinkers around the globe",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Segmented Control
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shadowElevation = 1.dp
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(24.dp))
                            .background(if (isSelected) Color(0xFFFF6D00) else Color.Transparent)
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(onClick = { selectedTab = tab }) {
                            Text(
                                text = tab,
                                color = if (isSelected) Color.White else Color(0xFF475569),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Leaderboard List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(MockData.users) { user ->
                LeaderboardCard(user = user)
            }
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun LeaderboardCard(user: LeaderboardUser) {
    val cardColor = when (user.rank) {
        1 -> Color(0xFFFFF9C4) // Gold tint
        2 -> Color(0xFFF5F5F5) // Silver tone
        3 -> Color(0xFFFFE0B2) // Bronze tint
        else -> Color.White
    }
    
    val iconColor = when (user.rank) {
        1 -> Color(0xFFFFC107) // Gold
        2 -> Color(0xFF9E9E9E) // Silver
        3 -> Color(0xFFFF5722) // Bronze
        else -> Color.Transparent
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if(user.rank <= 3) 2.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank Icon or Text
            Box(modifier = Modifier.width(32.dp), contentAlignment = Alignment.Center) {
                if (user.rank <= 3) {
                    Icon(Icons.Filled.EmojiEvents, contentDescription = "Rank", tint = iconColor)
                } else {
                    Text("${user.rank}", color = Color.Gray, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            
            // Avatar
            AsyncImage(
                model = "https://i.pravatar.cc/150?u=${user.id}",
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            
            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = user.location, color = Color.Gray, fontSize = 12.sp)
            }
            
            // Score
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "${user.drinks}", fontWeight = FontWeight.Light, fontSize = 20.sp)
                Text(text = "drinks", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}
