package com.example.repository

import com.example.model.local.User
import com.example.model.request.LoginRequest
import com.example.model.response.AuthResponse
import com.example.model.response.RefreshResponse

interface UserRepository {
    suspend fun getAllUsers(): List<User>
    suspend fun getUserById(id: String): User?
    suspend fun getUserByUsername(username: String): User?
    suspend fun insertUser(user: User): Boolean
    suspend fun deleteUserById(id: String): Boolean
    suspend fun deleteAllUsers(): Boolean
    suspend fun authenticate(loginRequest: LoginRequest): AuthResponse?
    suspend fun refresh(token: String): RefreshResponse?
}