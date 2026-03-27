package com.supdevinci.caisson.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.supdevinci.caisson.viewmodel.CocktailViewModel
import com.supdevinci.caisson.data.local.entities.CocktailEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: CocktailViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val categories = listOf("All", "Short (Shot)", "Long Drink", "Vodka", "Gin", "Rum")
    var selectedCategory by remember { mutableStateOf("All") }
    
    val cocktails by viewModel.allCocktails.collectAsState()
    val haptic = LocalHapticFeedback.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)) // Deep Obsidian Layer
    ) {
        // Hero Card (Dynamic Full Bleed)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b?q=80&w=2670&auto=format&fit=crop",
                    contentDescription = "Cocktail of the Week",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                
                // Heavy Fade Gradient for Text Contrast
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xFF0F172A)),
                            startY = 200f
                        ))
                )
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Surface(
                        color = Color(0xFFFF6D00).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "FEATURED",
                            color = Color(0xFFFF6D00),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "The Modern Art\nof Mixology.",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 36.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { 
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            val ranId = cocktails.randomOrNull()?.id
                            if(ranId != null) navController.navigate("cocktail_detail/$ranId")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text("DISCOVER BRAND NEW", color = Color(0xFF0F172A), fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Filled.ArrowForward, contentDescription = "Go", tint = Color(0xFF0F172A))
                    }
                }
            }
        }

        // White Content Area floating over Obsidian
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(Color(0xFFF8F9FA))
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                // Search Bar MD3
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search specific cocktails...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFFFF6D00)) },
                    modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Categories
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { 
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                selectedCategory = category 
                            },
                            label = { Text(category, fontWeight = FontWeight.SemiBold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF0F172A),
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            elevation = FilterChipDefaults.filterChipElevation(elevation = if(selectedCategory == category) 4.dp else 0.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                if (cocktails.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFFF6D00))
                    }
                } else {
                    val filtered = cocktails.filter { cocktail ->
                        val matchesSearch = if (searchQuery.isNotEmpty()) {
                            cocktail.name.contains(searchQuery, ignoreCase = true)
                        } else true
                        
                        // Advanced filtering using new network mapped 'category' + nested ingredients
                        val matchesCategory = when (selectedCategory) {
                            "All" -> true
                            "Short (Shot)" -> cocktail.category.contains("Shot", ignoreCase = true)
                            "Long Drink" -> cocktail.category.contains("Ordinary Drink", ignoreCase = true) || cocktail.category.contains("Cocktail", ignoreCase = true)
                            "Vodka" -> cocktail.ingredients.any { it.ingredient.contains("Vodka", ignoreCase = true) }
                            "Gin" -> cocktail.ingredients.any { it.ingredient.contains("Gin", ignoreCase = true) }
                            "Rum" -> cocktail.ingredients.any { it.ingredient.contains("Rum", ignoreCase = true) }
                            else -> true
                        }
                        
                        matchesSearch && matchesCategory
                    }
                    
                    if (filtered.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                            Text("No cocktails match your criteria.", color = Color.Gray)
                        }
                    } else {
                        filtered.forEach { cocktail ->
                            CocktailItemCard(cocktail) {
                                navController.navigate("cocktail_detail/${cocktail.id}")
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun CocktailItemCard(cocktail: CocktailEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .shadow(16.dp, RoundedCornerShape(32.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(32.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = cocktail.imageUrl,
                contentDescription = cocktail.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Frosted gradient bottom
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f)),
                        startY = 400f
                    ))
            )
            // Name Tag floating
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = cocktail.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${cocktail.category} • ${cocktail.ingredients.size} Ingredients",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha=0.7f)
                    )
                }
                
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha=0.2f)
                ) {
                    Icon(Icons.Filled.ArrowForward, contentDescription="View", modifier = Modifier.padding(12.dp), tint=Color.White)
                }
            }
        }
    }
}
