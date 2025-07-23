package com.example.Sin_Riesgo.domain.models

data class AuthInfo(
    val userId: String,
    val email: String?,
    val name: String?,
    val profileImageUrl: String? = null // <-- ¡CRÍTICO! Esta línea debe estar.
)