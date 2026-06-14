package com.entrelacos.arandu.model

data class SupportPoint(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val createdAt: Long = 0
)