package com.example.Sin_Riesgo.screens // PAQUETE CRÍTICO: DEBE COINCIDIR CON LA RUTA REAL

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.Sin_Riesgo.componets.FooterBase
import com.example.Sin_Riesgo.componets.HeaderBase
import com.example.Sin_Riesgo.componets.CameraCaptureButton
import com.example.Sin_Riesgo.viewmodels.GiveReportViewModel // IMPORTACIÓN CRÍTICA
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GiveReportScreen(
    onBack: () -> Unit,
    navController: NavController,
    reportViewModel: GiveReportViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        reportViewModel.reportEvent.collect { event ->
            when (event) {
                is GiveReportViewModel.ReportEvent.Success -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
                is GiveReportViewModel.ReportEvent.Error -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = { FooterBase(navController = navController) },
        topBar = { HeaderBase("DAR REPORTE", onBack = onBack) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF050533))
                .padding(paddingValues)
                .padding(horizontal = 30.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            ReportForm(
                reportViewModel = reportViewModel,
                navController = navController,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ReportForm(
    reportViewModel: GiveReportViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                reportViewModel.onDateIncidentChange(year, month, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IncidentDropdownField(
            "TIPO DE INCIDENTE",
            reportViewModel.tipeIncident,
            reportViewModel::onTipeIncidentChange
        )

        ReportField(
            "DESCRIPCIÓN",
            reportViewModel.descripIncident,
            reportViewModel::onDescripIncidentChange,
            "Ingrese una descripción..."
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "FECHA",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .clickable { datePickerDialog.show() }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = if (reportViewModel.dateIncident.isNotEmpty()) reportViewModel.dateIncident else "Selecciona una fecha...",
                    color = if (reportViewModel.dateIncident.isNotEmpty()) Color.Black else Color.Gray,
                    fontSize = 16.sp
                )
            }
        }

        ReportField(
            "UBICACIÓN",
            reportViewModel.placeIncident,
            reportViewModel::onPlaceIncidentChange,
            "Ingrese ubicación"
        )

        CameraCaptureButton(
            onImageCaptured = reportViewModel::onImageCaptured,
            modifier = Modifier.fillMaxWidth()
        )

        reportViewModel.fotoBitmap?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Imagen tomada:", color = Color.White, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Foto tomada",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        GiveReportSendButtons(
            isLoading = reportViewModel.isLoading,
            onSubmitClick = { isAnonymous -> reportViewModel.submitReport(isAnonymous) }
        )
    }
}

@Composable
fun GiveReportSendButtons(
    isLoading: Boolean,
    onSubmitClick: (Boolean) -> Unit
) {
    ActionButton(
        icon = Icons.AutoMirrored.Filled.HelpOutline,
        text = if (isLoading) "ENVIANDO..." else "REPORTE ANÓNIMO",
        height = 55.dp,
        onClick = { onSubmitClick(true) },
        enabled = !isLoading
    )

    Spacer(modifier = Modifier.height(8.dp))

    Button(
        onClick = { onSubmitClick(false) },
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(horizontal = 60.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
        shape = RoundedCornerShape(12.dp),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
        } else {
            Icon(Icons.Filled.Send, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("ENVIAR REPORTE", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun ReportField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )
    }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 50.dp,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .padding(horizontal = 60.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        enabled = enabled
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.Black,
            fontSize = 16.sp
        )
    }
}

@Composable
fun IncidentDropdownField(
    label: String,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val incidentOptions = listOf("Robo", "Acoso", "Violencia", "Vandalismo", "Otro")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (selectedOption.isNotEmpty()) selectedOption else "Selecciona un tipo...",
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Abrir menú",
                    tint = Color.Black
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(320.dp)
            ) {
                incidentOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option, fontSize = 16.sp, color = Color.Black) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}