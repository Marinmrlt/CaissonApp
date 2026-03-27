package com.supdevinci.caisson.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.supdevinci.caisson.viewmodel.CocktailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAddScreen(navController: NavController, viewModel: CocktailViewModel, snackbarHostState: SnackbarHostState) {
    val profile by viewModel.userProfile.collectAsState()
    val cocktails by viewModel.allCocktails.collectAsState()
    val liveBac by viewModel.liveBac.collectAsState()
    val recentDrinks by viewModel.recentDrinks.collectAsState()

    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    var quantity by remember { mutableStateOf(1) }
    var expanded by remember { mutableStateOf(false) }
    var selectedCocktailId by remember { mutableStateOf<String?>(null) }
    
    val selectedCocktailName = selectedCocktailId?.let { id -> 
        cocktails.find { it.id == id }?.name 
    } ?: "Choose a cocktail..."

    val bacFloat = liveBac
    val bacStatus = when {
        bacFloat < 0.02f -> "Sober"
        bacFloat < 0.06f -> "Tipsy"
        bacFloat < 0.15f -> "Drunk"
        else -> "Danger"
    }
    
    val targetBacColor = when {
        bacFloat < 0.02f -> Color(0xFF059669) // Emerald Green 600
        bacFloat < 0.06f -> Color(0xFFD97706) // Warning Yellow 600
        bacFloat < 0.15f -> Color(0xFFDC2626) // Red 600
        else -> Color(0xFF991B1B) // Dark Red 800
    }
    
    // Using clean white for the massive card base to match the light theme background
    val targetBgColor = Color.White

    val bacColor by animateColorAsState(targetValue = targetBacColor, animationSpec = tween(500))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)) // Uniform Light Background
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Active Tracker", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
        Text("Live Blood Alcohol Content", color = Color.Gray, modifier = Modifier.padding(bottom = 32.dp))

        // Massive Dynamic BAC Card (Light Theme)
        Card(
            modifier = Modifier.fillMaxWidth().shadow(12.dp, RoundedCornerShape(32.dp)),
            colors = CardDefaults.cardColors(containerColor = targetBgColor),
            shape = RoundedCornerShape(32.dp)
        ) {
            Column(modifier = Modifier.padding(32.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if(bacFloat > 0.15f) Icons.Filled.Warning else Icons.Filled.Info, 
                        contentDescription = "Status", 
                        tint = bacColor, 
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(bacStatus.uppercase(), color = bacColor, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = String.format("%.3f", bacFloat),
                        fontSize = 56.sp, fontWeight = FontWeight.Light, color = bacColor, letterSpacing = (-2).sp
                    )
                    Text("%", color = bacColor, fontSize = 24.sp, modifier = Modifier.padding(bottom = 12.dp, start = 4.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Beautiful light grey background indicator for profile stats
                Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(8.dp)) {
                    Text("Calculated for ${profile.gender}, ${profile.weightKg}kg", color = Color.DarkGray, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Add Drink Card (Light Theme)
        Card(
            modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFFFF6D00).copy(alpha=0.1f), shape = RoundedCornerShape(12.dp)) {
                        Icon(Icons.Filled.LocalDrink, contentDescription = "Drink", tint = Color(0xFFFF6D00), modifier = Modifier.padding(8.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Record a Drink", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                }
                Spacer(modifier = Modifier.height(24.dp))
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCocktailName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF8F9FA),
                            unfocusedContainerColor = Color(0xFFF8F9FA),
                            focusedTextColor = Color(0xFF0F172A),
                            unfocusedTextColor = Color(0xFF0F172A),
                            focusedBorderColor = Color(0xFFFF6D00),
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        if (cocktails.isEmpty()) {
                            DropdownMenuItem(text = { Text("Loading DB...", color = Color.Gray) }, onClick = {})
                        } else {
                            cocktails.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.name, color = Color(0xFF0F172A)) },
                                    onClick = {
                                        selectedCocktailId = option.id
                                        expanded = false
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Quantity", fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { 
                            if (quantity > 1) {
                                quantity--
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.size(56.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("-", color = Color(0xFF0F172A), fontSize = 28.sp) }
                    
                    Text("$quantity", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    
                    Button(
                        onClick = { 
                            quantity++ 
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.size(56.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("+", color = Color(0xFF0F172A), fontSize = 28.sp) }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (selectedCocktailId != null) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            
                            viewModel.addDrink(
                                cocktailId = selectedCocktailId!!,
                                name = selectedCocktailName,
                                quantity = quantity
                            )
                            
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Drink logged to BAC history!",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            
                            quantity = 1
                            selectedCocktailId = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(24.dp),
                    enabled = selectedCocktailId != null
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("LOG DRINK TO BAC", color = Color.White, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("Past 24 Hours", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
        Spacer(modifier = Modifier.height(16.dp))
        
        if(recentDrinks.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().height(80.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text("Sober clear! No drinks logged.", color = Color.Gray)
                }
            }
        } else {
            recentDrinks.forEach { drink ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(drink.cocktailName, color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                            Text("${drink.volumeMl}ml • ${(drink.abvEstimated * 100).toInt()}% ABV", color = Color.Gray, fontSize = 12.sp)
                        }
                        Surface(color = Color(0xFFFF6D00).copy(alpha=0.1f), shape = RoundedCornerShape(8.dp)) {
                            val timeString = android.text.format.DateFormat.format("hh:mm a", drink.timestamp).toString()
                            Text(timeString, color = Color(0xFFFF6D00), fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 12.sp)
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(120.dp))
    }
}
