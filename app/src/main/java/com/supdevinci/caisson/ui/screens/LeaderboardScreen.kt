package com.supdevinci.caisson.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(navController: NavController) {
    val rankedUsers = remember { MockData.users.sortedBy { it.rank } }
    val topUser = rankedUsers.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.EmojiEvents,
                        contentDescription = "Trophy",
                        tint = Color(0xFFFF6D00),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Leaderboard",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A)
                    )
                }
                Text(
                    "Classement base sur les donnees mock integrees",
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        if (topUser != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(28.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Surface(
                            color = Color(0xFFFF6D00).copy(alpha = 0.16f),
                            shape = RoundedCornerShape(999.dp)
                        ) {
                            Text(
                                "TOP PERFORMER",
                                color = Color(0xFFFFB37A),
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            topUser.name,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(topUser.location, color = Color.White.copy(alpha = 0.72f))
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            LeaderboardMetric(
                                label = "Rank",
                                value = "#${topUser.rank}",
                                accent = Color(0xFFFFD166)
                            )
                            LeaderboardMetric(
                                label = "Drinks",
                                value = topUser.drinks.toString(),
                                accent = Color(0xFF7DD3FC)
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                "Full ranking",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        }

        items(rankedUsers, key = { it.id }) { user ->
            LeaderboardRankCard(user = user)
        }

        item {
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
private fun LeaderboardMetric(label: String, value: String, accent: Color) {
    Surface(
        color = Color.White.copy(alpha = 0.08f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Text(label, color = Color.White.copy(alpha = 0.65f), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = accent, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun LeaderboardRankCard(user: LeaderboardUser) {
    val (bgColor, textColor, badgeColor) = when (user.rank) {
        1 -> Triple(Color(0xFFFEF9C3), Color(0xFF854D0E), Color(0xFFEAB308)) // Gold
        2 -> Triple(Color(0xFFF1F5F9), Color(0xFF334155), Color(0xFF94A3B8)) // Silver
        3 -> Triple(Color(0xFFFFEDD5), Color(0xFF9A3412), Color(0xFFF97316)) // Bronze
        else -> Triple(Color.White, Color.Black, Color(0xFFE2E8F0)) // Normal
    }

    val elevation = if (user.rank <= 3) 12.dp else 2.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
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
                        text = "#${user.rank}",
                        fontWeight = FontWeight.Black,
                        color = badgeColor,
                        fontSize = 18.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = textColor
                )
                Text(
                    text = user.location,
                    color = textColor.copy(alpha=0.7f),
                    fontSize = 14.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${user.drinks}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = textColor
                )
                Text(
                    text = "drinks",
                    color = textColor.copy(alpha = 0.65f),
                    fontSize = 12.sp
                )
            }

            if (user.rank <= 3) {
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    Icons.Filled.EmojiEvents,
                    contentDescription = "Trophy",
                    tint = badgeColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
