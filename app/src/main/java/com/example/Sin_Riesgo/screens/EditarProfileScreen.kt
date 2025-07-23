package com.example.Sin_Riesgo.screens // Paquete ajustado a tu estructura

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // Para usar Modifier.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter // Importa esta librería para Coil
import com.example.Sin_Riesgo.componets.HeaderBase
import com.example.Sin_Riesgo.viewmodels.EditProfileViewModel // Asegúrate de esta importación

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    navController: NavController, // NavController se mantiene para acciones específicas de navegación de la UI
    editProfileViewModel: EditProfileViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Launcher para seleccionar una imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            editProfileViewModel.onImageSelected(uri)
        }
    )

    LaunchedEffect(Unit) {
        editProfileViewModel.editProfileEvent.collect { event ->
            when (event) {
                is EditProfileViewModel.EditProfileEvent.Success -> {
                    // Muestra el mensaje y navega hacia atrás
                    snackbarHostState.showSnackbar(event.message)
                    navController.popBackStack()
                }
                is EditProfileViewModel.EditProfileEvent.Error -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        topBar = { HeaderBase("EDITAR PERFIL", onBack = onBack) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color(0xFF050533)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Selector de imagen de perfil
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape) // Recorta la imagen a un círculo
                    .background(Color.LightGray)
                    .clickable { imagePickerLauncher.launch("image/*") } // Abre la galería
                    .border(2.dp, Color.White, CircleShape) // Borde blanco alrededor del círculo
            ) {
                if (editProfileViewModel.profileImageUri != null) {
                    // Carga la imagen desde la URI local seleccionada
                    Image(
                        painter = rememberAsyncImagePainter(editProfileViewModel.profileImageUri),
                        contentDescription = "Imagen de perfil seleccionada",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Aquí podrías mostrar la imagen actual del usuario si la tuvieras cargada en el ViewModel
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Imagen de perfil por defecto",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        tint = Color.Gray
                    )
                }
            }
            Text("Cambiar foto", color = Color.White, modifier = Modifier.padding(top = 8.dp))

            Spacer(modifier = Modifier.height(32.dp))

            // Campo para cambiar el nombre
            OutlinedTextField(
                value = editProfileViewModel.name,
                onValueChange = { editProfileViewModel.onNameChange(it) },
                label = { Text("Nombre de Usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botón para guardar cambios
            Button(
                onClick = { editProfileViewModel.saveProfile() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !editProfileViewModel.isLoading // Deshabilita el botón si está cargando
            ) {
                if (editProfileViewModel.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("GUARDAR CAMBIOS", fontSize = 16.sp)
                }
            }
        }
    }
}