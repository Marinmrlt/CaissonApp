package com.supdevinci.caisson.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.supdevinci.caisson.viewmodel.CocktailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetailScreen(navController: NavController, viewModel: CocktailViewModel, id: String) {
    val cocktail by viewModel.getCocktailById(id).collectAsState()

    if (cocktail == null) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0F172A)), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFFF6D00))
        }
        return
    }

    val cd = cocktail!!

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            
            // Hero Image
            Box(modifier = Modifier.fillMaxWidth().height(450.dp)) {
                AsyncImage(
                    model = cd.imageUrl,
                    contentDescription = cd.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                
                // Deep Fade Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xFF0F172A).copy(alpha=0.9f), Color(0xFFF8F9FA)),
                            startY = 300f
                        ))
                )

                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = cd.name,
                        color = Color(0xFF0F172A),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 44.sp,
                        letterSpacing = (-1).sp
                    )
                }
            }

            // Body
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                
                // Ingredients
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Science, contentDescription = "Ingredients", tint = Color(0xFFFF6D00), modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("The Recipe", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                }
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth().shadow(12.dp, RoundedCornerShape(24.dp))
                ) {
                    Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
                        cd.ingredients.forEachIndexed { index, item ->
                            if(item.ingredient.isNotBlank()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Filled.CheckCircle, contentDescription="check", tint = Color(0xFF34D399), modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(text = item.ingredient, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A), fontSize = 16.sp, modifier = Modifier.weight(1f))
                                    Text(text = item.measure ?: "to taste", color = Color.Gray, fontSize = 14.sp)
                                }
                                if(index != cd.ingredients.lastIndex) {
                                    HorizontalDivider(color = Color(0xFFF1F5F9))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Instructions
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.MenuBook, contentDescription = "Instructions", tint = Color(0xFFFF6D00), modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Instructions", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                }
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth().shadow(16.dp, RoundedCornerShape(24.dp))
                ) {
                    Text(
                        text = cd.instructions,
                        color = Color.White.copy(alpha=0.9f),
                        fontSize = 16.sp,
                        lineHeight = 28.sp,
                        modifier = Modifier.padding(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Floating Back App Bar
        TopAppBar(
            title = { },
            navigationIcon = {
                Surface(
                    onClick = { navController.popBackStack() },
                    shape = CircleShape,
                    color = Color.Black.copy(alpha=0.4f),
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp).size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            },
            actions = {
                Surface(
                    onClick = { viewModel.toggleFavorite(cd.id, !cd.isFavorite) },
                    shape = CircleShape,
                    color = Color.Black.copy(alpha=0.4f),
                    modifier = Modifier.padding(end = 12.dp, top = 12.dp).size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            if (cd.isFavorite) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = "Save", 
                            tint = if (cd.isFavorite) Color(0xFFFF6D00) else Color.White
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
    }
}
