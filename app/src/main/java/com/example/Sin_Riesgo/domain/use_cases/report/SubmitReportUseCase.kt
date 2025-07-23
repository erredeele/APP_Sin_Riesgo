package com.example.Sin_Riesgo.domain.use_cases.report

import android.graphics.Bitmap
import com.example.Sin_Riesgo.data.repositories.ReportRepository

class SubmitReportUseCase(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(
        type: String,
        description: String,
        date: String,
        location: String,
        image: Bitmap?
    ): Result<Unit> {
        if (type.isBlank() || description.isBlank() || date.isBlank() || location.isBlank()) {
            return Result.failure(IllegalArgumentException("Todos los campos obligatorios deben ser completados."))
        }
        return try {
            val success = reportRepository.submitReport(type, description, date, location, image)
            if (success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Fallo al enviar el reporte. Inténtalo de nuevo más tarde."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}