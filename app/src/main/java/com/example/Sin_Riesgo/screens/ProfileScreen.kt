package com.example.Sin_Riesgo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip // <-- ¡IMPORTANTE! Para Modifier.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter // ¡IMPORTANTE para cargar imágenes desde URL!
import com.example.Sin_Riesgo.componets.FooterBase
import com.example.Sin_Riesgo.componets.HeaderBase
import com.example.Sin_Riesgo.viewmodels.LoginViewModel
import com.example.Sin_Riesgo.navigation.Screens

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    navController: NavController,
    onEditProfileClick: () -> Unit, // <--- ¡NUEVO CALLBACK AÑADIDO!
    loginViewModel: LoginViewModel = viewModel()
) {
    val currentUserInfo by loginViewModel.currentUserInfo.collectAsState()
    val userName = currentUserInfo?.name ?: "No disponible"
    val userEmail = currentUserInfo?.email ?: "No disponible"
    val userId = currentUserInfo?.userId ?: "No disponible"
    val profileImageUrl = currentUserInfo?.profileImageUrl // Obtener la URL de la imagen

    LaunchedEffect(Unit) {
        loginViewModel.loginEvent.collect { event ->
            if (event is LoginViewModel.LoginEvent.LoggedOut) {
                navController.navigate(Screens.Welcome.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050533))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeaderBase("PERFIL", onBack = onBack)

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF2C3E50), RoundedCornerShape(20.dp))
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(CircleShape) // Recorta la imagen a un círculo
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                if (!profileImageUrl.isNullOrEmpty()) {
                                    // Carga la imagen desde la URL usando Coil
                                    Image(
                                        painter = rememberAsyncImagePainter(profileImageUrl),
                                        contentDescription = "Imagen de perfil del usuario",
                                        contentScale = ContentScale.Crop, // Escala para llenar el círculo
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    // Muestra el icono por defecto si no hay URL o es nula/vacía
                                    Icon(
                                        imageVector = Icons.Filled.Person,
                                        contentDescription = "User Icon",
                                        modifier = Modifier.size(150.dp),
                                        tint = Color.Black
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = userName,
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 20.dp)
                            )

                            ProfileField("ID de Usuario: $userId")
                            ProfileField("CORREO: $userEmail")
                            ProfileField("NUMERO DE REPORTES: 17")
                            ProfileField("ULTIMO REPORTE: 24/05/25")

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Contactos de Emergencia",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            ProfileField("Policía: 105")
                            ProfileField("Bomberos: 116")
                            ProfileField("Ambulancia: 106")

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = onEditProfileClick, // <-- ¡AQUÍ SE CONECTA EL CALLBACK!
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF082736))
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Editar Perfil")
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { loginViewModel.logout() },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF082736))
                            ) {
                                Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Cerrar Sesión")
                            }
                        }
                    }
                }
            }

            FooterBase(navController = navController)
        }
    }
}

@Composable
fun ProfileField(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color(0xFFDADADA), RoundedCornerShape(8.dp))
            .padding(20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = text, color = Color.Black, fontSize = 15.sp)
    }
}