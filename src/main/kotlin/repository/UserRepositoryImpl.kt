package com.example.repository

import com.auth0.jwt.interfaces.DecodedJWT
import com.example.model.local.TokenEntity
import com.example.model.local.User
import com.example.model.request.LoginRequest
import com.example.model.response.AuthResponse
import com.example.model.response.RefreshResponse
import com.example.security.JwtService
import kotlinx.html.InputAutoComplete.username

class UserRepositoryImpl(
    private val userDatabase: UserDatabase,
    private val tokenDatabase: TokenDatabase,
    private val jwtService: JwtService
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
        } else false
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

    override suspend fun authenticate(loginRequest: LoginRequest): AuthResponse? {
        val foundedUser = getUserByUsername(loginRequest.username) ?: return null
        return if (foundedUser.password == loginRequest.password) {
            val accessToken = jwtService.createAccessToken(foundedUser.username, foundedUser.password)
            val refreshToken = jwtService.createRefreshToken(foundedUser.username, foundedUser.password)
            tokenDatabase.save(TokenEntity(username = foundedUser.username, token = refreshToken))
            AuthResponse(accessToken = accessToken, refreshToken = refreshToken)
        } else null
    }

    override suspend fun refresh(token: String): RefreshResponse? {
        val refreshTokenDecoded = decodedJwtToken(token) ?: return null
        val usernameInTokenDB = tokenDatabase.getUsernameByToken(token) ?: return null
        val foundedUser = getUserByUsername(usernameInTokenDB) ?: return null

        return if (foundedUser.username == refreshTokenDecoded.getClaim("username").asString()) {
            val accessToken = jwtService.createAccessToken(foundedUser.username, foundedUser.password)
            RefreshResponse(accessToken = accessToken)
        } else null
    }

    private fun decodedJwtToken(token: String): DecodedJWT? {
        val decodeJwt = try {
            jwtService.jwtVerifier().verify(token)
        } catch (e: Exception) {
            null
        }
        return decodeJwt?.let {
            if (jwtService.audienceMatchByRefresh(it.audience.first())) {
                decodeJwt
            } else null
        }
    }


}