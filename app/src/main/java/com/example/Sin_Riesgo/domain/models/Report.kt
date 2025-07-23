package com.example.Sin_Riesgo.domain.models

import android.graphics.Bitmap

data class Report(
    val id: String,
    val type: String,
    val description: String,
    val date: String,
    val location: String,
    val imageUrl: String?
)