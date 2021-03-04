package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.models.dto.UserDto
import com.marks0mmers.budgetcreator.models.persistent.User
import com.marks0mmers.budgetcreator.models.views.AuthRequestView
import com.marks0mmers.budgetcreator.models.views.CreateUserView
import com.marks0mmers.budgetcreator.services.UserService
import com.marks0mmers.budgetcreator.config.security.JWTUtil
import com.marks0mmers.budgetcreator.util.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*

@Configuration
class UserController(val userService: UserService, val jwtUtil: JWTUtil) {
    @Bean
    fun userRouter() = coRouter {
        "/api/users".nest {
            GET("/current") { req ->
                val principal = req.awaitPrincipal() ?: fail("Cannot get Principal")
                val user = userService.getUserByUsername(principal.name)
                ok().json()
                    .bodyValueAndAwait(addToken(user))
            }

            POST { req ->
                val body = req.awaitBody<CreateUserView>()
                val createdUser = userService.createUser(body)
                ok().json()
                    .bodyValueAndAwait(createdUser)
            }

            POST("/login") { req ->
                val body = req.awaitBody<AuthRequestView>()
                val user = userService.login(body.username, body.password)
                ok().json()
                    .bodyValueAndAwait(addToken(user))
            }

            GET("/{userId}") { req ->
                val userId = req.pathVariable("userId")
                val user = userService.getUserById(userId)
                ok().json()
                    .bodyValueAndAwait(user)
            }
        }
    }

    private fun addToken(user: UserDto): UserDto {
        user.token = jwtUtil.generateToken(user)
        return user
    }

    private fun addToken(user: User): UserDto {
        val userDto = UserDto(user)
        userDto.token = jwtUtil.generateToken(user)
        return userDto
    }
}