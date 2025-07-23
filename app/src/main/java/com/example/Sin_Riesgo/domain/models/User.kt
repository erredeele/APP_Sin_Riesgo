package com.example.Sin_Riesgo.domain.models

data class User(
    val id: String,
    val email: String,
    val name: String,
    val phoneNumber: String?,
    val reportCount: Int
)