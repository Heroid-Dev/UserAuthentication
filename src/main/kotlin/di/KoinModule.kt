package com.example.di

import com.example.repository.UserDatabase
import com.example.repository.UserRepository
import com.example.repository.UserRepositoryImpl
import io.ktor.server.application.Application
import org.koin.dsl.module

val koinModule = { application: Application ->
    module {
        single {
            UserDatabase()
        }
        factory<UserRepository> {
            UserRepositoryImpl(get<UserDatabase>())
        }
    }
}