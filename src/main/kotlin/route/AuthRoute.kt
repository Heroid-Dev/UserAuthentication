package com.example.route

import com.example.model.request.LoginRequest
import com.example.security.JwtService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.getKoin

fun Route.authRoute() {
    val jwtService = application.getKoin().get<JwtService>()
    post("login") {
        val loginRequest = call.receive<LoginRequest>()
        return@post jwtService.createJwtToken(loginRequest)?.let { accessToken ->
            call.respond(message = hashMapOf("token" to accessToken))
        } ?: call.respond(HttpStatusCode.Unauthorized)
    }
}