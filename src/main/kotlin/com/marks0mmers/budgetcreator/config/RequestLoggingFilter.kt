package com.marks0mmers.budgetcreator.config

import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import reactor.core.publisher.Mono

/**
 * A web filter to log incoming requests and outgoing responses
 *
 * @see Ordered
 * @see WebFilter
 * @author Mark Sommers
 */
class RequestLoggingFilter : Ordered by Ordered({ -999 }), WebFilter by WebFilter({ swe, wfc ->
    /**
     * Gets a string representation of the HTTP Request
     *
     * @param exchange The SWE to get the request from
     * @return The string version of the request
     */
    fun getRequestMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val method = request.method
        val path = request.uri.path
        val acceptableMediaTypes = request.headers.accept
        val contentType = request.headers.contentType
        return ">>>REQUEST>>> $method $path $ACCEPT: $acceptableMediaTypes ${CONTENT_TYPE}: $contentType"
    }

    /**
     * Gets a string representation of the HTTP Response
     *
     * @param exchange The SWE to get the response from
     * @return The string version of the response
     */
    fun getResponseMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val response = exchange.response
        val method = request.method
        val path = request.uri.path
        val statusCode = response.statusCode ?: HttpStatus.CONTINUE
        val contentType = response.headers.contentType
        return "<<<RESPONSE<<< $method $path HTTP${statusCode.value()} ${statusCode.reasonPhrase} ${CONTENT_TYPE}: $contentType"
    }

    val logger = LoggerFactory.getLogger(RequestLoggingFilter::class.java)
    logger.info(getRequestMessage(swe))
    val filter = wfc.filter(swe)
    swe.response.beforeCommit {
        logger.info(getResponseMessage(swe))
        Mono.empty()
    }
    filter
})