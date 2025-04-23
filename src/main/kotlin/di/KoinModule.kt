package com.example.di

import com.example.repository.TokenDatabase
import com.example.repository.UserDatabase
import com.example.repository.UserRepository
import com.example.repository.UserRepositoryImpl
import com.example.security.JwtService
import io.ktor.server.application.Application
import org.koin.dsl.module

val koinModule = { application: Application ->
    module {
        factory {
            UserDatabase()
        }
        factory {
            JwtService(application, get<UserDatabase>())
        }
        factory {
            TokenDatabase()
        }
        factory<UserRepository> {
            UserRepositoryImpl(
                get<UserDatabase>(),
                get<TokenDatabase>(),
                get<JwtService>())
        }
    }
}