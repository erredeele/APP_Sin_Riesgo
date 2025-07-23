package com.example.Sin_Riesgo.domain.use_cases.auth

import com.example.Sin_Riesgo.data.repositories.AuthRepository
import com.example.Sin_Riesgo.domain.models.AuthInfo

class SignInWithGoogleUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(googleIdToken: String, email: String?, name: String?): Result<AuthInfo> {
        if (googleIdToken.isBlank()) {
            return Result.failure(IllegalArgumentException("Token de Google no puede estar vacío"))
        }
        return authRepository.signInWithGoogle(googleIdToken, email, name)
    }
}