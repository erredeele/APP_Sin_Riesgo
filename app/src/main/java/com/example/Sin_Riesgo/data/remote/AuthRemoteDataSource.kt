package com.example.Sin_Riesgo.data.remote

import kotlinx.coroutines.delay

class AuthRemoteDataSource {

    suspend fun signInWithGoogleBackend(googleIdToken: String): String {
        delay(1000)
        println("Simulando intercambio de Google ID Token en el backend: $googleIdToken")
        return "mock_user_id_${googleIdToken.takeLast(5)}"
    }

    suspend fun requestPhoneOtp(phoneNumber: String): Boolean {
        delay(1500)
        println("Simulando envío de OTP al número: $phoneNumber")
        return true
    }

    suspend fun verifyPhoneOtp(phoneNumber: String, otp: String): String {
        delay(1000)
        if (otp == "123456") {
            println("Simulando verificación de OTP para $phoneNumber, exitosa")
            return "mock_user_id_phone_${phoneNumber.takeLast(4)}"
        } else {
            throw Exception("Código OTP inválido")
        }
    }
}