package com.example.airmovies.util

sealed class RegisterValidation() {
    object Success: RegisterValidation()
    data class Failed(val message: String): RegisterValidation()
}

data class RegisterFieldState(
    val email: RegisterValidation,
    val password: RegisterValidation,
    val username: RegisterValidation
)

data class LoginFieldState(
    val email: RegisterValidation,
    val password: RegisterValidation
)