package com.example.Sin_Riesgo.domain.use_cases.auth

import com.example.Sin_Riesgo.data.repositories.AuthRepository
import com.example.Sin_Riesgo.domain.models.AuthInfo

class LoginUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthInfo> {
        return try {
            val authInfoResult = authRepository.login(email, password)
            authInfoResult
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}