package com.entrelacos.arandu.model

data class Post(

    val id: String = "",

    val userId: String = "",

    val userName: String = "",

    val userPhoto: String = "",

    val text: String = "",

    val likes: Int = 0,

    val comments: Int = 0,

    val createdAt: Long = 0
)