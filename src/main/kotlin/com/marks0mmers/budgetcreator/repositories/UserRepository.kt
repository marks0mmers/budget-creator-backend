package com.marks0mmers.budgetcreator.repositories

import com.marks0mmers.budgetcreator.models.persistent.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserRepository : ReactiveMongoRepository<User, String> {
    fun findByUsername(username: String): Mono<User>
}