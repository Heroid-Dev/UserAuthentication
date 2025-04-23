package com.example.repository

import com.example.model.local.User
import org.litote.kmongo.eq

interface UserRepository {
    suspend fun getAllUsers(): List<User>
    suspend fun getUserById(id: String): User?
    suspend fun getUserByUsername(username: String): User?
    suspend fun insertUser(user: User): Boolean
    suspend fun deleteUserById(id: String): Boolean
    suspend fun deleteAllUsers(): Boolean
}