package com.example.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val refreshToken: String,
    val accessToken: String
)
