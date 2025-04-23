package com.example.repository

import com.example.model.local.TokenEntity
import com.example.utils.Constant.DATABASE_NAME
import com.example.utils.Constant.DATABASE_URL
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo


class TokenDatabase {
    private val client = KMongo.createClient(DATABASE_URL).coroutine
    private val database = client.getDatabase(DATABASE_NAME)
    private val tokenCollection = database.getCollection<TokenEntity>()

    suspend fun save(tokenEntity: TokenEntity): Boolean =
        getUsernameByToken(tokenEntity.token)?.let {
            update(tokenEntity)
        } ?: tokenCollection.insertOne(tokenEntity).wasAcknowledged()


    suspend fun getUsernameByToken(token: String): String? =
        tokenCollection.findOne(TokenEntity::token eq token)?.username

    suspend fun update(tokenEntity: TokenEntity): Boolean =
        tokenCollection.updateOneById(tokenEntity.username, tokenEntity).wasAcknowledged()

}