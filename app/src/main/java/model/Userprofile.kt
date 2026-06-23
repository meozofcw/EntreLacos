package com.entrelacos.arandu.model

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val bio: String = "",
    val city: String = "",
    val notificationsEnabled: Boolean = true,
    val childName: String = "",
    val childAge: String = "",
    val childCondition: String = ""
)