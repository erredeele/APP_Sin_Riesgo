package com.example.Sin_Riesgo.componets

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.Sin_Riesgo.navigation.Screens

@Composable
fun FooterBase(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val activeColor = Color(0xFFC0CA33) // Un color que contraste para el item seleccionado
    val inactiveColor = Color.LightGray // Mantener el color inactivo

    NavigationBar(
        containerColor = Color(0xFF050533),
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = currentRoute == Screens.Menu.route,
            onClick = {
                if (currentRoute != Screens.Menu.route) {
                    navController.navigate(Screens.Menu.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = "Inicio",
                    tint = if (currentRoute == Screens.Menu.route) activeColor else inactiveColor,
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == Screens.Walk.route,
            onClick = {
                if (currentRoute != Screens.Walk.route) {
                    navController.navigate(Screens.Walk.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Buscar",
                    tint = if (currentRoute == Screens.Walk.route) activeColor else inactiveColor,
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == Screens.Give.route,
            onClick = {
                if (currentRoute != Screens.Give.route) {
                    navController.navigate(Screens.Give.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Agregar",
                    tint = if (currentRoute == Screens.Give.route) activeColor else inactiveColor,
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == Screens.Reports.route,
            onClick = {
                if (currentRoute != Screens.Reports.route) {
                    navController.navigate(Screens.Reports.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = {
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = "Notificaciones",
                    tint = if (currentRoute == Screens.Reports.route) activeColor else inactiveColor,
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == Screens.Profile.route,
            onClick = {
                if (currentRoute != Screens.Profile.route) {
                    navController.navigate(Screens.Profile.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Perfil",
                    tint = if (currentRoute == Screens.Profile.route) activeColor else inactiveColor,
                    modifier = Modifier.size(30.dp)
                )
            }
        )
    }
}