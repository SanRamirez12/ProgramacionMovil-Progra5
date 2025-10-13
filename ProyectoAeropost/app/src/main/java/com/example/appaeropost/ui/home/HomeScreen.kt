package com.example.appaeropost.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var count by remember { mutableStateOf(0) }

    Column(modifier = modifier.padding(16.dp)) {
        Text("Inicio Aeropost")
        Spacer(Modifier.height(8.dp))
        Text("Contador: $count")
        Spacer(Modifier.height(8.dp))
        Button(onClick = { count++ }) { Text("Sumar 1") }
    }
}
