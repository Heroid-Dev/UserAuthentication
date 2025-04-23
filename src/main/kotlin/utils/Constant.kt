package com.example.utils

object Constant {
    private val username= System.getenv("USERNAME")?: throw IllegalStateException("username not found")
    private val password= System.getenv("PASSWORD")?: throw IllegalStateException("password not found")
    val DATABASE_URL= "mongodb+srv://$username:$password@cluster0.fbhg5vw.mongodb.net/"
    const val DATABASE_NAME = "KtorJwtV1"
}