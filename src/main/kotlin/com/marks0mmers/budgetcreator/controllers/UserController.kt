package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.config.security.JWTUtil
import com.marks0mmers.budgetcreator.models.dto.UserDto
import com.marks0mmers.budgetcreator.models.views.AuthRequestView
import com.marks0mmers.budgetcreator.models.views.CreateUserView
import com.marks0mmers.budgetcreator.services.UserService
import com.marks0mmers.budgetcreator.util.POST
import com.marks0mmers.budgetcreator.util.fail
import org.springframework.web.reactive.function.server.*

/**
 * The API Controller for User related functionality
 *
 * @see RouterFunction
 * @see CoRouterFunctionDsl
 * @constructor
 * Injects the parameters as dependencies
 *
 * @param userService The user service containing business logic
 * @param jwtUtil The JWT Utility functions used to generate tokens
 */
class UserController(
    userService: UserService,
    jwtUtil: JWTUtil
) : RouterFunction<ServerResponse> by coRouter({

    /**
     * A helper function that adds a token to a user
     *
     * @return The user that has a token attached to it
     */
    fun UserDto.addToken(): UserDto {
        token = jwtUtil.generateToken(username, roles)
        return this
    }

    "/api/users".nest {
        GET("/current") { req ->
            val principal = req.awaitPrincipal() ?: fail("Cannot get Principal")
            val user = userService.getUserByUsername(principal.name)
            ok().json()
                .bodyValueAndAwait(user.addToken())
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
                .bodyValueAndAwait(user.addToken())
        }

        GET("/{userId}") { req ->
            val userId = req.pathVariable("userId").toInt()
            val user = userService.getUserById(userId)
            ok().json()
                .bodyValueAndAwait(user)
        }
    }
})