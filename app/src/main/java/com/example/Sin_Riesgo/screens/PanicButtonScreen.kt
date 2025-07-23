package com.example.Sin_Riesgo.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.Sin_Riesgo.componets.FooterBase
import com.example.Sin_Riesgo.componets.HeaderBase
import com.example.Sin_Riesgo.R

@Composable
fun PanicButtonScreen(
    onBack: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050533)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderBase("BOTÓN DE PÁNICO", onBack = onBack)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "SIN",
            fontSize = 35.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "RIESGO",
            fontSize = 35.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Icon(
            Icons.Filled.Notifications,
            contentDescription = null,
            tint = Color.Red,
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Image(
            painter = painterResource(id = R.drawable.boton_panico),
            contentDescription = "Botón de Pánico",
            modifier = Modifier
                .size(200.dp)
                .clickable {
                    Toast
                        .makeText(context, "¡Botón de Pánico Presionado!", Toast.LENGTH_SHORT)
                        .show()
                }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF354864))
                .padding(horizontal = 15.dp, vertical = 5.dp)
        ) {
            Switch(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Color(0xFF050533),
                    checkedTrackColor = Color(0xFFC0CA33),
                    uncheckedTrackColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "Presionar 3 veces botón de volumen",
                color = Color.White,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        FooterBase(navController = navController)
    }
}