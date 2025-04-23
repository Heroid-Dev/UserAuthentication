package com.example.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RefreshResponse(
    val accessToken: String
)
