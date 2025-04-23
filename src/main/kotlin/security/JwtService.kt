package com.example.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.request.LoginRequest
import com.example.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import java.sql.Date

class JwtService(
    private val application: Application,
    private val userRepository: UserRepository
) {
    private val audience = getConfigProperties("jwt.audience")
    private val issuer = getConfigProperties("jwt.issuer")
    private val secret = getConfigProperties("jwt.secret")
    val realm = getConfigProperties("jwt.realm")


    private fun getConfigProperties(path: String): String =
        application.environment.config.property(path).getString()

    fun jwtVerifier(): JWTVerifier =
        JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()

    suspend fun createJwtToken(loginRequest: LoginRequest): String? {
        val foundedUser = userRepository.getUserByUsername(loginRequest.username) ?: return null
        return if (foundedUser.password == loginRequest.password) {
            JWT
                .create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("username", foundedUser.username)
                .withClaim("password", foundedUser.password)
                .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                .sign(Algorithm.HMAC256(secret))
        } else null
    }

    suspend fun customValidator(credential: JWTCredential): JWTPrincipal? {
        val username = credential.payload.getClaim("username").asString()
        val password = credential.payload.getClaim("password").asString()
        val foundedUser = userRepository.getUserByUsername(username) ?: return null
        return if (foundedUser.password == password) {
            if (matchAudience(credential)) {
                JWTPrincipal(credential.payload)
            } else null
        } else null
    }

    private fun matchAudience(credential: JWTCredential): Boolean =
        credential.payload.audience.contains(audience)

}