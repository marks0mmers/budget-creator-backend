package com.marks0mmers.budgetcreator.config.security

import com.marks0mmers.budgetcreator.models.persistent.Role
import com.marks0mmers.budgetcreator.util.PropertyValue
import com.marks0mmers.budgetcreator.util.instant
import com.marks0mmers.budgetcreator.util.javaDate
import io.jsonwebtoken.*
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import java.util.*

/**
 * A utility class for common JWT functionality
 */
class JWTUtil {
    /**
     * The expiration date configured for JWT tokens
     * @see PropertyValue
     */
    private val expiration: Long by PropertyValue("json-web-token.jjwt") { it?.toLong() ?: 0 }

    /**
     * The secret configured for JWT tokens
     * @see PropertyValue
     */
    private val secret: String by PropertyValue("json-web-token.jjwt") { it ?: "" }

    /**
     * The encoded secret calculated lazily using [Base64]
     * @see lazy
     */
    private val encodedSecret: String by lazy { Base64.getEncoder().encodeToString(secret.toByteArray()) }

    /**
     * Validates the token based on the expiration date
     *
     * @param token The encoded JWT token
     * @return Whether the token is valid or not
     */
    fun validateToken(token: String) = getAllClaimsFromToken(token)
        ?.expiration
        ?.instant
        ?.let { it > Clock.System.now() }
        ?: false

    /**
     * Get all the claims from the JWT token
     *
     * @param token The encoded JWT token
     * @return The claims object of the JWT
     * @see Claims
     */
    fun getAllClaimsFromToken(token: String) = try {
        Jwts
            .parser()
            .setSigningKey(encodedSecret)
            .parseClaimsJws(token)
            .body
    } catch (e: Exception) {
        null
    }

    /**
     * Generates a JWT Token from a username and a list of roles
     *
     * @param username The username to put into the token
     * @param roles The roles to put into the token
     * @return The Encoded JWT token
     */
    fun generateToken(username: String, roles: List<Role>): String = Jwts.builder()
        .claim("role", roles)
        .setSubject(username)
        .setIssuedAt(Clock.System.now().javaDate)
        .setExpiration(Clock.System.now().plus(expiration, DateTimeUnit.SECOND).javaDate)
        .signWith(SignatureAlgorithm.HS512, encodedSecret)
        .compact()
}