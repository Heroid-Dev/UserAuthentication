package com.example.repository

import com.example.model.local.User
import com.example.utils.Constant.DATABASE_NAME
import com.example.utils.Constant.DATABASE_URL
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

class UserDatabase {
    private val client= KMongo.createClient(DATABASE_URL).coroutine
    private val database= client.getDatabase(DATABASE_NAME)
    private val userCollection = database.getCollection<User>()

    suspend fun getAllUsers(): List<User> =
        userCollection.find().toList()

    suspend fun getUserById(id: String): User? =
        userCollection.findOneById(id)

    suspend fun getUserByUsername(username: String): User? =
        userCollection.findOne(User::username eq username)

    suspend fun insertUser(user: User): Boolean =
        userCollection.insertOne(user).wasAcknowledged()

    suspend fun deleteUserById(id: String): Boolean =
        userCollection.deleteOneById(id).wasAcknowledged()

    suspend fun deleteAllUsers(): Boolean =
        userCollection.deleteMany("{}").wasAcknowledged()
}