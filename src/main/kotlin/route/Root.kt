package com.example.route

import io.ktor.server.routing.Route
import io.ktor.server.routing.route

fun Route.root(){
    route("user"){
        userRoute()
    }
}