package com.example.Sin_Riesgo.domain.use_cases.auth

import android.net.Uri
import com.example.Sin_Riesgo.data.repositories.AuthRepository

class UpdateUserProfileUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(newName: String, newImageUri: Uri?): Result<Unit> {
        if (newName.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre no puede estar vacío."))
        }
        return authRepository.updateUserProfile(newName, newImageUri)
    }
}