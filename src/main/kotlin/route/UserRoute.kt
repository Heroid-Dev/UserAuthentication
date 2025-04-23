package com.example.route

import com.example.model.local.User
import com.example.model.request.LoginRequest
import com.example.model.response.UserResponse
import com.example.repository.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.koin.ktor.ext.getKoin
import java.util.UUID

fun Route.userRoute() {
    val userRepository = application.getKoin().get<UserRepository>()

    get("all") {
        userRepository.getAllUsers().let { listOfUser ->
            if (listOfUser.isEmpty()) {
                return@get call.respond(
                    message = "No users found",
                    status = HttpStatusCode.NoContent
                )
            } else {
                return@get call.respond(
                    message = listOfUser.map(User::toResponse)
                )
            }
        }
    }

    post("add") {
        val loginRequest = call.receive<LoginRequest>()
        userRepository.insertUser(loginRequest.toMode()).let {
            if (it) {
                return@post call.respond(
                    message = "User added successfully",
                    status = HttpStatusCode.Created
                )
            } else {
                return@post call.respond(
                    message = "User already exists",
                    status = HttpStatusCode.Conflict
                )
            }
        }
    }

    get("get") {
        val id = call.request.queryParameters["id"] ?: return@get call.respond(
            message = "No id",
            status = HttpStatusCode.BadRequest
        )
        return@get userRepository.getUserById(id)?.let { user ->
            call.respond(
                message = user.toResponse(),
                status = HttpStatusCode.OK
            )
        } ?: call.respond(
            message = "User not found",
            status = HttpStatusCode.NotFound
        )
    }
    delete("all") {
        return@delete userRepository.deleteAllUsers().let {
            if (it) {
                call.respond(
                    message = "Users deleted successfully",
                    status = HttpStatusCode.OK
                )
            } else {
                call.respond(
                    message = "Users list empty",
                    status = HttpStatusCode.NotFound
                )
            }
        }
    }
}

private fun LoginRequest.toMode(): User =
    User(
        id = UUID.randomUUID().toString(),
        username = this.username,
        password = this.password,
    )

fun User.toResponse(): UserResponse =
    UserResponse(
        id = this.id,
        username = this.username
    )