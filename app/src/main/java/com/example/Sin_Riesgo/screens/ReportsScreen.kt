package com.example.Sin_Riesgo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.Sin_Riesgo.componets.FooterBase
import com.example.Sin_Riesgo.R
import android.content.Intent
import android.widget.Toast
import com.example.Sin_Riesgo.componets.HeaderBase

@Composable
fun ReportsScreen(
    onBack: () -> Unit,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050533))
    ) {
        HeaderBase("REPORTES", onBack = onBack)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(
                listOf(
                    Triple(
                        "Robo",
                        "Descripción del incidente, puede ser personalizado por el usuario y deberá mostrarse a otros usuarios.",
                        "20/05/25"
                    ),
                    Triple(
                        "Asalto",
                        "Un incidente fue reportado en la calle principal cerca del mercado central.",
                        "19/05/25"
                    ),
                    Triple(
                        "Hurto",
                        "Se reportó la sustracción de una bicicleta cerca del parque municipal.",
                        "18/05/25"
                    ),
                    Triple(
                        "Vandalismo",
                        "Pintadas y daños a la propiedad pública durante la noche.",
                        "17/05/25"
                    ),
                    Triple(
                        "Robo",
                        "Robo de celular en la zona universitaria. Sospechoso no identificado.",
                        "16/05/25"
                    )
                )
            ) { (title, content, date) ->
                ReportCardBase(title, content, date)
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        FooterBase(navController = navController)
    }
}

@Composable
fun ReportCardBase(title: String, content: String, date: String) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .background(Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.perfil),
                        contentDescription = null
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = content,
                        fontSize = 18.sp,
                        maxLines = 2
                    )
                    Text(
                        text = date,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                IconButton(onClick = {
                    try {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "🚨 Reporte de Incidente 🚨\n\nTítulo: $title\nDescripción: $content\nFecha: $date\n\nReportado desde la app Riesgo Cero."
                            )
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(
                            Intent.createChooser(
                                shareIntent,
                                "Compartir reporte"
                            )
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "Error al compartir: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = "Compartir"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    // Acción al hacer clic en "Ver todo"
                }) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Ver todo",
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ver todo", color = Color.Gray)
                }
            }
        }
    }
}