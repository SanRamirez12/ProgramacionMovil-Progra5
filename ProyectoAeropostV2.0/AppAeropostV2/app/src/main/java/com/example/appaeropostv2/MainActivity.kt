package com.example.appaeropostv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.appaeropostv2.core.designsystem.theme.AeropostTheme
import com.example.appaeropostv2.presentation.common.layout.AppScaffold
import com.example.appaeropostv2.presentation.common.navigation.AppBottomBar
import com.example.appaeropostv2.presentation.common.navigation.AppNavGraph
import com.example.appaeropostv2.presentation.common.navigation.BottomBarScreens

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
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        AppScaffold(
            topBar = { /* Top bar global si la necesitas */ },
            bottomBar = {
                // Solo mostramos el BottomBar en las pantallas principales,
                // no en Login u otras pantallas "full screen".
                val bottomRoutes = BottomBarScreens.map { it.route }
                if (currentRoute in bottomRoutes) {
                    AppBottomBar(navController)
                }
            }
        ) {
            AppNavGraph(navController = navController)
        }
    }
}


