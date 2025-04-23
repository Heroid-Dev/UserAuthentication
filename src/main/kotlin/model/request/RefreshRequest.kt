package com.example.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    val refreshToken: String
)
