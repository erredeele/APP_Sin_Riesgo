package com.example.Sin_Riesgo.data.repositories

import android.content.Context
import android.net.Uri
import com.example.Sin_Riesgo.data.local.AuthLocalDataSource
import com.example.Sin_Riesgo.data.remote.AuthRemoteDataSource
import com.example.Sin_Riesgo.domain.models.AuthInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.delay

class AuthRepository(private val context: Context) {

    private val authLocalDataSource = AuthLocalDataSource(context)
    private val authRemoteDataSource = AuthRemoteDataSource()

    val authInfo: Flow<AuthInfo?> = authLocalDataSource.authInfo
    val userEmail: Flow<String?> = authLocalDataSource.userEmail
    val isLoggedIn: Flow<Boolean> = authLocalDataSource.isLoggedIn

    // Modificado: Ahora acepta profileImageUrl en saveUserSession
    suspend fun login(email: String, password: String): Result<AuthInfo> {
        kotlinx.coroutines.delay(1000)
        val success = email == "renzoguzman@gmail.com" && password == "123456789"
        return if (success) {
            val userId = "local_user_id_123"
            val name = "Usuario Local"
            val imageUrl = "https://example.com/default_profile.jpg" // URL de imagen de perfil por defecto simulada
            authLocalDataSource.saveUserSession(email, name, userId, imageUrl) // Pasar URL
            Result.success(AuthInfo(userId, email, name, imageUrl)) // Pasar URL
        } else {
            Result.failure(Exception("Credenciales inválidas"))
        }
    }

    // Modificado: Ahora acepta profileImageUrl en saveUserSession
    suspend fun signInWithGoogle(googleIdToken: String, email: String?, name: String?): Result<AuthInfo> {
        return try {
            val userId = authRemoteDataSource.signInWithGoogleBackend(googleIdToken)
            // Google a veces proporciona URL de imagen, podrías obtenerla de GoogleSignInAccount
            val imageUrl = "https://example.com/google_profile_pic.jpg" // URL de imagen de Google simulada
            authLocalDataSource.saveUserSession(email, name, userId, imageUrl) // Pasar URL
            Result.success(AuthInfo(userId, email, name, imageUrl)) // Pasar URL
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun requestPhoneOtp(phoneNumber: String): Result<Unit> {
        return try {
            val success = authRemoteDataSource.requestPhoneOtp(phoneNumber)
            if (success) Result.success(Unit) else Result.failure(Exception("Fallo al solicitar OTP"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Modificado: Ahora acepta profileImageUrl en saveUserSession
    suspend fun verifyPhoneOtp(phoneNumber: String, otp: String): Result<AuthInfo> {
        return try {
            val userId = authRemoteDataSource.verifyPhoneOtp(phoneNumber, otp)
            val name = "Usuario Teléfono ${phoneNumber.takeLast(4)}"
            val imageUrl = "https://example.com/phone_default_profile.jpg" // URL de imagen de perfil por defecto simulada
            authLocalDataSource.saveUserSession(phoneNumber, name, userId, imageUrl) // Pasar URL
            Result.success(AuthInfo(userId, phoneNumber, name, imageUrl)) // Pasar URL
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clearUserSession() {
        authLocalDataSource.clearUserSession()
    }

    // Modificado: Ahora el caso de uso devuelve la URL de imagen final
    suspend fun updateUserProfile(newName: String, newImageUri: Uri?): Result<Unit> {
        return try {
            var finalImageUrl: String? = null

            if (newImageUri != null) {
                println("Simulando subida de imagen desde: $newImageUri")
                delay(1500)
                // En una app real: Subir newImageUri a Firebase Storage y obtener la URL de descarga
                // finalImageUrl = uploadImageToFirebaseStorage(newImageUri)
                finalImageUrl = "https://example.com/user_uploaded_image_${System.currentTimeMillis()}.jpg" // URL simulada
            }

            println("Simulando actualización del nombre a: $newName")
            delay(1000)

            val currentUserInfo = authInfo.firstOrNull() // Obtiene el último valor del flujo
            if (currentUserInfo != null) {
                // Si no se subió una nueva imagen, se mantiene la URL existente
                val imageUrlToSave = finalImageUrl ?: currentUserInfo.profileImageUrl
                authLocalDataSource.saveUserSession(
                    email = currentUserInfo.email,
                    name = newName,
                    userId = currentUserInfo.userId,
                    profileImageUrl = imageUrlToSave // <-- Guardar la URL final
                )
                Result.success(Unit)
            } else {
                Result.failure(Exception("No se encontró la sesión de usuario para actualizar el perfil localmente."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}