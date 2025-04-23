package com.example.repository

import com.example.model.local.User
import kotlinx.html.InputAutoComplete.username

class UserRepositoryImpl(
    private val userDatabase: UserDatabase
) : UserRepository {

    override suspend fun getAllUsers(): List<User> =
        userDatabase.getAllUsers()

    override suspend fun getUserById(id: String): User? =
        userDatabase.getUserById(id)

    override suspend fun getUserByUsername(username: String): User? =
        userDatabase.getUserByUsername(username)

    override suspend fun insertUser(user: User): Boolean {
        return if (userDatabase.getUserByUsername(username) == null) {
            userDatabase.insertUser(user)
        }else false
    }

    override suspend fun deleteUserById(id: String): Boolean {
        return if (userDatabase.getUserById(id) != null) {
            userDatabase.deleteUserById(id)
        } else false
    }

    override suspend fun deleteAllUsers(): Boolean {
        return if (userDatabase.getAllUsers().isNotEmpty()) {
            userDatabase.deleteAllUsers()
        } else false
    }
}