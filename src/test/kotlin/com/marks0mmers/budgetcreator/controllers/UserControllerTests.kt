package com.marks0mmers.budgetcreator.controllers

import com.marks0mmers.budgetcreator.config.security.JWTUtil
import com.marks0mmers.budgetcreator.models.persistent.Role
import com.marks0mmers.budgetcreator.models.dto.UserDto
import com.marks0mmers.budgetcreator.models.views.AuthRequestView
import com.marks0mmers.budgetcreator.models.views.CreateUserView
import com.marks0mmers.budgetcreator.services.UserService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(SpringExtension::class)
@WebFluxTest
@Import(UserController::class, JWTUtil::class)
@WithMockUser(username = "marks0mmers", roles = ["USER"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserControllerTests {
    @MockBean lateinit var userService: UserService
    @Autowired lateinit var webClient: WebTestClient

    private val userDto = UserDto(
        id = 1,
        username = "marks0mmers",
        firstName = "Mark",
        lastName = "Sommers",
        enabled = true,
        roles = listOf(Role.ROLE_USER)
    )

    @BeforeEach
    fun configure() {
        webClient = webClient.mutateWith(csrf())
    }

    @Test
    fun `test logging in`(): Unit = runBlocking {
        `when`(userService.login(userDto.username, "Truckin09")).thenReturn(userDto)

        webClient.post()
            .uri("/api/users/login")
            .accept(APPLICATION_JSON)
            .bodyValue(AuthRequestView("marks0mmers", "Truckin09"))
            .exchange()
            .expectStatus().isOk
            .expectBody(UserDto::class.java)
    }

    @Test
    fun `test creating user`(): Unit = runBlocking {
        val createUserView = CreateUserView(
            username = "marks0mmers",
            password = "Truckin09",
            firstName = "Mark",
            lastName = "Sommers"
        )

        `when`(userService.createUser(createUserView)).thenReturn(userDto)

        webClient.post()
            .uri("/api/users")
            .accept(APPLICATION_JSON)
            .bodyValue(createUserView)
            .exchange()
            .expectStatus().isOk
            .expectBody(UserDto::class.java)

        verify(userService).createUser(createUserView)
    }

    @Test
    fun `test getting current user`(): Unit = runBlocking {
        `when`(userService.getUserByUsername(userDto.username)).thenReturn(userDto)

        webClient.get()
            .uri("/api/users/current")
            .exchange()
            .expectStatus().isOk
            .expectBody(UserDto::class.java)
    }

    @Test
    fun `test getting user by id`(): Unit = runBlocking {
        `when`(userService.getUserById(userDto.id)).thenReturn(userDto)

        webClient.get()
            .uri("/api/users/${userDto.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody(UserDto::class.java)
    }
}