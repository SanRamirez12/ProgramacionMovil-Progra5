package com.example.appaeropostv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.appaeropostv2.core.designsystem.theme.AeropostTheme
import com.example.appaeropostv2.presentation.common.layout.AppScaffold
import com.example.appaeropostv2.presentation.common.navigation.AppBottomBar
import com.example.appaeropostv2.presentation.common.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { RootApp() }
    }
}

@Composable
fun RootApp() {
    AeropostTheme {
        val navController = rememberNavController()
        AppScaffold(
            topBar = { /* Top bar global si la necesitas */ },
            bottomBar = { AppBottomBar(navController) }
        ) {
            AppNavGraph(navController = navController)
        }
    }
}

