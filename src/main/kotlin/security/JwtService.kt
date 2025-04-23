package com.example.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.repository.UserDatabase
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import java.sql.Date

class JwtService(
    private val application: Application,
    private val db: UserDatabase
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

    fun createAccessToken(username: String, password: String): String =
        createJwtToken(username, password, 3_600_000)

    fun createRefreshToken(username: String, password: String): String =
        createJwtToken(username, password, 86_400_000)

    fun audienceMatchByRefresh(audience: String): Boolean =
        this.audience == audience

    private fun createJwtToken(username: String, password: String, expireIn: Long): String =
        JWT
            .create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", username)
            .withClaim("password", password)
            .withExpiresAt(Date(System.currentTimeMillis() + expireIn))
            .sign(Algorithm.HMAC256(secret))


    suspend fun customValidator(credential: JWTCredential): JWTPrincipal? {
        val username = credential.payload.getClaim("username").asString()
        val password = credential.payload.getClaim("password").asString()
        val foundedUser = db.getUserByUsername(username) ?: return null
        return if (foundedUser.password == password) {
            if (matchAudience(credential)) {
                JWTPrincipal(credential.payload)
            } else null
        } else null
    }

    private fun matchAudience(credential: JWTCredential): Boolean =
        credential.payload.audience.contains(audience)

}