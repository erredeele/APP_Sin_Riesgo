package com.example.Sin_Riesgo.viewmodels // PAQUETE CRÍTICO: DEBE COINCIDIR CON LA RUTA REAL

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Sin_Riesgo.data.repositories.ReportRepository
import com.example.Sin_Riesgo.domain.use_cases.report.SubmitReportUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class GiveReportViewModel : ViewModel() {

    private val submitReportUseCase = SubmitReportUseCase(ReportRepository())

    var tipeIncident by mutableStateOf("")
        private set
    var descripIncident by mutableStateOf("")
        private set
    var dateIncident by mutableStateOf("")
        private set
    var placeIncident by mutableStateOf("")
        private set
    var fotoBitmap by mutableStateOf<Bitmap?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set

    private val _reportEvent = MutableSharedFlow<ReportEvent>()
    val reportEvent = _reportEvent.asSharedFlow()

    fun onTipeIncidentChange(newType: String) {
        tipeIncident = newType
    }

    fun onDescripIncidentChange(newDescription: String) {
        descripIncident = newDescription
    }

    fun onDateIncidentChange(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateIncident = format.format(calendar.time)
    }

    fun onPlaceIncidentChange(newPlace: String) {
        placeIncident = newPlace
    }

    fun onImageCaptured(bitmap: Bitmap) {
        fotoBitmap = bitmap
    }

    fun clearImage() {
        fotoBitmap = null
    }

    fun submitReport(isAnonymous: Boolean) {
        if (isLoading) return

        isLoading = true
        viewModelScope.launch {
            val result = submitReportUseCase(
                tipeIncident,
                descripIncident,
                dateIncident,
                placeIncident,
                fotoBitmap
            )
            isLoading = false
            when {
                result.isSuccess -> {
                    _reportEvent.emit(ReportEvent.Success("Reporte enviado con éxito"))
                    resetFields()
                }
                result.isFailure -> {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido al enviar el reporte"
                    _reportEvent.emit(ReportEvent.Error(errorMessage))
                }
            }
        }
    }

    private fun resetFields() {
        tipeIncident = ""
        descripIncident = ""
        dateIncident = ""
        placeIncident = ""
        fotoBitmap = null
    }

    sealed class ReportEvent {
        data class Success(val message: String) : ReportEvent()
        data class Error(val message: String) : ReportEvent()
    }
}