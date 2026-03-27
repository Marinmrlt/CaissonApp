package com.supdevinci.caisson.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.supdevinci.caisson.viewmodel.CocktailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAddScreen(navController: NavController, viewModel: CocktailViewModel) {
    val profile by viewModel.userProfile.collectAsState()
    val cocktails by viewModel.allCocktails.collectAsState()

    var quantity by remember { mutableStateOf(1) }
    var expanded by remember { mutableStateOf(false) }
    var selectedCocktailId by remember { mutableStateOf<String?>(null) }
    
    val selectedCocktailName = selectedCocktailId?.let { id -> 
        cocktails.find { it.id == id }?.name 
    } ?: "Choose a cocktail..."

    // BAC Placeholder Logic (Widmark proxy)
    // Normally we track consumed alcohol volume. For UI, let's pretend 0 for now.
    val calculatedBac = "0.000%"
    val bacStatus = "Sober"
    val bacColor = Color(0xFF16A34A)
    val bacBgColor = Color(0xFFF0FDF4)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)) // Light bg
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Quick Add",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )
        Text(
            text = "Track your drinks and monitor your BAC",
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // BAC Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = bacBgColor),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Add, contentDescription = "Drop", tint = bacColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Blood Alcohol Content", color = Color(0xFF334155), fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = calculatedBac,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Light,
                        color = bacColor
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = bacStatus, color = bacColor, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Based on ${profile.gender.lowercase()}, ${profile.age}y, ${profile.weightKg}kg", color = Color.Gray, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Add a Drink Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Add a Drink", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Select Cocktail", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                
                // Dropdown from API list
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
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        if (cocktails.isEmpty()) {
                            DropdownMenuItem(text = { Text("Loading...") }, onClick = {})
                        } else {
                            cocktails.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.name) },
                                    onClick = {
                                        selectedCocktailId = option.id
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Quantity", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { if (quantity > 1) quantity-- },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(48.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("-", color = Color.Black, fontSize = 24.sp) }
                    
                    Text("$quantity", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    
                    Button(
                        onClick = { quantity++ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(48.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("+", color = Color.Black, fontSize = 24.sp) }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { /* Implement BAC active tracking logic here */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Drink", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("Today's Drinks", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth().height(80.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text("No drinks added today", color = Color.Gray)
            }
        }
        
        Spacer(modifier = Modifier.height(100.dp)) // padding for bottom bar
    }
}
