package com.example.plugins

import com.example.security.JwtService
import io.ktor.server.application.*
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.jwt
import org.koin.ktor.ext.getKoin

fun Application.configureSecurity() {
    val jwtService = getKoin().get<JwtService>()
    authentication {
        jwt {
            realm = jwtService.realm
            verifier {
                jwtService.jwtVerifier()
            }
            validate { credential ->
                jwtService.customValidator(credential)
            }
        }
    }
}
