package com.example.Sin_Riesgo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.Sin_Riesgo.navigation.Screens
import com.example.Sin_Riesgo.screens.* // Asegúrate de que esta importación sea correcta
import com.example.Sin_Riesgo.ui.theme.NuevoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = false

        setContent {
            NuevoTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFF050533)
                ) { innerPadding ->
                    AppNavigation(navController, Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screens.Welcome.route,
        modifier = modifier
    ) {
        composable(Screens.Welcome.route) {
            WelcomeScreen(
                onContinueClicked = {
                    navController.navigate(Screens.Login.route)
                }
            )
        }

        composable(Screens.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screens.Menu.route) {
                        popUpTo(Screens.Welcome.route) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(Screens.Menu.route) {
            MenuScreen(
                onPerfilClick = { navController.navigate(Screens.Profile.route) },
                onReportesClick = { navController.navigate(Screens.Reports.route) },
                onDarReporteClick = { navController.navigate(Screens.Give.route) },
                onCaminaSeguroClick = { navController.navigate(Screens.Walk.route) },
                onBotonPanicoClick = { navController.navigate(Screens.Panic.route) },
                navController = navController
            )
        }

        composable(Screens.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                navController = navController,
                onEditProfileClick = { // <-- ¡AQUÍ SE DEFINE EL CALLBACK PARA NAVEGAR!
                    navController.navigate(Screens.EditProfile.route)
                }
            )
        }
        composable(Screens.Reports.route) {
            ReportsScreen(
                onBack = { navController.popBackStack() },
                navController = navController
            )
        }
        composable(Screens.Give.route) {
            GiveReportScreen(
                onBack = { navController.popBackStack() },
                navController = navController
            )
        }
        composable(Screens.Walk.route) {
            WalkSafelyScreen(
                onBack = { navController.popBackStack() },
                navController = navController
            )
        }
        composable(Screens.Panic.route) {
            PanicButtonScreen(
                onBack = { navController.popBackStack() },
                navController = navController
            )
        }
        // <--- ¡AÑADIR ESTE NUEVO COMPOSABLE PARA LA PANTALLA DE EDICIÓN!
        composable(Screens.EditProfile.route) {
            EditProfileScreen(
                onBack = { navController.popBackStack() },
                navController = navController
            )
        }
        composable("anonymous_report") {
            // Destino placeholder
        }
    }
}