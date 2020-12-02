package com.marks0mmers.budgetcreator.models.dto

data class AuthRequest (
        val username: String,
        val password: String
)

data class AuthResponse (
        val token: String
)

data class Message (
        val content: String
)