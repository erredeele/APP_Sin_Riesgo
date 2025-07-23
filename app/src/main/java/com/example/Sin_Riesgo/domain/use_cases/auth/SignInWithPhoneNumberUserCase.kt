package com.example.Sin_Riesgo.domain.use_cases.auth

import com.example.Sin_Riesgo.data.repositories.AuthRepository
import com.example.Sin_Riesgo.domain.models.AuthInfo

class SignInWithPhoneNumberUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun requestOtp(phoneNumber: String): Result<Unit> {
        if (phoneNumber.isBlank()) {
            return Result.failure(IllegalArgumentException("Número de teléfono no puede estar vacío"))
        }
        return authRepository.requestPhoneOtp(phoneNumber)
    }

    suspend fun verifyOtp(phoneNumber: String, otp: String): Result<AuthInfo> {
        if (otp.isBlank()) {
            return Result.failure(IllegalArgumentException("Código OTP no puede estar vacío"))
        }
        return authRepository.verifyPhoneOtp(phoneNumber, otp)
    }
}