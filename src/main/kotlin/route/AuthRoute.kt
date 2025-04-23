package com.example.route

import com.example.model.request.LoginRequest
import com.example.model.request.RefreshRequest
import com.example.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.getKoin

fun Route.authRoute() {
    val userRepository = application.getKoin().get<UserRepository>()
    post("login") {
        val loginRequest = call.receive<LoginRequest>()
        return@post userRepository.authenticate(loginRequest)?.let {
            call.respond(it)
        } ?: call.respond(HttpStatusCode.Unauthorized)
    }
    post("refresh") {
        val refreshRequest = call.receive<RefreshRequest>()
        return@post userRepository.refresh(refreshRequest.refreshToken)?.let {
            call.respond(it)
        } ?: call.respond(HttpStatusCode.Unauthorized)
    }
}