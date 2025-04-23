package com.example.model.local

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class TokenEntity(
    @BsonId
    val username: String,
    val token: String,
)
