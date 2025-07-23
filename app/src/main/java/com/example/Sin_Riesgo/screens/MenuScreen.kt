package com.example.Sin_Riesgo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.Sin_Riesgo.componets.FooterBase
import com.example.Sin_Riesgo.viewmodels.LoginViewModel

@Composable
fun MenuScreen(
    onPerfilClick: () -> Unit,
    onReportesClick: () -> Unit,
    onDarReporteClick: () -> Unit,
    onCaminaSeguroClick: () -> Unit,
    onBotonPanicoClick: () -> Unit,
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {
    val currentUserInfo by loginViewModel.currentUserInfo.collectAsState()
    val userName = currentUserInfo?.name ?: currentUserInfo?.email ?: "Invitado"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050533)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomHeader("¡HOLA, ${userName.uppercase()}!")

        Spacer(modifier = Modifier.height(10.dp))

        HorizontalDivider(
            color = Color.White,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MenuButton("PERFIL", onClick = onPerfilClick)
            MenuButton("REPORTES", onClick = onReportesClick)
            MenuButton("DAR REPORTE", onClick = onDarReporteClick)
            MenuButton("CAMINA SEGURO", onClick = onCaminaSeguroClick)
            MenuButton("BOTÓN DE PÁNICO", onClick = onBotonPanicoClick)
        }

        FooterBase(navController = navController)
    }
}

@Composable
fun CustomHeader(text: String) {
    Text(
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .padding(top = 40.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
    )
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Ir a $text",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}