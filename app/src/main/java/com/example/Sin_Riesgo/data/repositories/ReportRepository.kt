package com.example.Sin_Riesgo.data.repositories

import android.graphics.Bitmap
import kotlinx.coroutines.delay

class ReportRepository {
    suspend fun submitReport(
        type: String,
        description: String,
        date: String,
        location: String,
        image: Bitmap?
    ): Boolean {
        delay(1500)
        println("Reporte enviado: Tipo=$type, Desc=$description, Fecha=$date, Lugar=$location, Imagen=${image != null}")
        return true
    }
}