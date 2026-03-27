package com.supdevinci.caisson.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("World", "National", "Regional")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.EmojiEvents, contentDescription = "Trophy", tint = Color(0xFFFF6D00), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Leaderboard",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A)
            )
        }
        Text("Top mixologists around the globe", color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
        
        Spacer(modifier = Modifier.height(24.dp))

        // Custom Floating Segmented Tab rows
        Surface(
            modifier = Modifier.fillMaxWidth().height(56.dp).shadow(8.dp, RoundedCornerShape(28.dp)),
            color = Color.White,
            shape = RoundedCornerShape(28.dp)
        ) {
            Row(modifier = Modifier.fillMaxSize().padding(4.dp)) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Button(
                        onClick = { selectedTab = index },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color(0xFF0F172A) else Color.Transparent
                        ),
                        shape = RoundedCornerShape(24.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        if(index == 0) Icon(Icons.Filled.Public, contentDescription = "World", modifier = Modifier.size(16.dp), tint = if (isSelected) Color.White else Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = title,
                            color = if (isSelected) Color.White else Color.Gray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(10) { index ->
                val rank = index + 1
                LeaderboardRankCard(rank = rank, tabIndex = selectedTab)
                Spacer(modifier = Modifier.height(16.dp))
            }
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun LeaderboardRankCard(rank: Int, tabIndex: Int) {
    val (bgColor, textColor, badgeColor) = when (rank) {
        1 -> Triple(Color(0xFFFEF9C3), Color(0xFF854D0E), Color(0xFFEAB308)) // Gold
        2 -> Triple(Color(0xFFF1F5F9), Color(0xFF334155), Color(0xFF94A3B8)) // Silver
        3 -> Triple(Color(0xFFFFEDD5), Color(0xFF9A3412), Color(0xFFF97316)) // Bronze
        else -> Triple(Color.White, Color.Black, Color(0xFFE2E8F0)) // Normal
    }

    val elevation = if(rank <= 3) 12.dp else 2.dp
    
    // Dynamic mock names depending on the selected tab
    val leaderName = when(tabIndex) {
        0 -> if(rank == 1) "Alex Mercer" else "World User $rank"
        1 -> if(rank == 1) "Sarah Jenkins" else "National User $rank"
        else -> if(rank == 1) "Local Legend" else "Regional User $rank"
    }
    
    // Dynamic mock points depending on tab
    val basePoints = when(tabIndex) {
        0 -> 10000
        1 -> 5000
        else -> 1000
    }
    val points = basePoints - (rank * (basePoints / 20))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .shadow(elevation, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank Badge
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = badgeColor.copy(alpha=0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "#$rank",
                        fontWeight = FontWeight.Black,
                        color = badgeColor,
                        fontSize = 18.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = leaderName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = textColor
                )
                Text(
                    text = "$points Points",
                    color = textColor.copy(alpha=0.7f),
                    fontSize = 14.sp
                )
            }
            
            if (rank <= 3) {
                Icon(Icons.Filled.EmojiEvents, contentDescription="Trophy", tint=badgeColor, modifier=Modifier.size(24.dp))
            }
        }
    }
}
