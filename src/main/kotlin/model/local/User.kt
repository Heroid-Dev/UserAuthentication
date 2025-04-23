package com.example.model.local

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class User(
    @BsonId
    val id: String,
    val username: String,
    val password: String
)
