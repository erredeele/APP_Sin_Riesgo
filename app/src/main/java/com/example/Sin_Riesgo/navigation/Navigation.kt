package com.example.Sin_Riesgo.navigation

sealed class Screens(val route: String) {
    object Welcome : Screens("WelcomeScreen")
    object Login : Screens("LoginScreen")
    object Menu : Screens("MenuScreen")
    object Profile : Screens("ProfileScreen")
    object Reports : Screens("ReportsScreen")
    object Give : Screens("GiveReportScreen")
    object Walk : Screens("WalkSafelyScreen")
    object Panic : Screens("PanicButtonScreen")
    object EditProfile : Screens("EditProfileScreen") // <-- ¡NUEVA RUTA!
}