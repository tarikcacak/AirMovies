package com.example.airmovies.data.user

import android.text.Editable

data class User (
    val username: String,
    val email: String,
    val password: String,
    val imgPath: String = "",
)