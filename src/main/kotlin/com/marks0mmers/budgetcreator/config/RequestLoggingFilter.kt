package com.marks0mmers.budgetcreator.config

import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class RequestLoggingFilter : WebFilter, Ordered {
    private val logger = LoggerFactory.getLogger(RequestLoggingFilter::class.java)

    override fun getOrder() = -999

    private fun getRequestMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val method = request.method
        val path = request.uri.path
        val acceptableMediaTypes = request.headers.accept
        val contentType = request.headers.contentType
        return ">>>REQUEST>>> $method $path $ACCEPT: $acceptableMediaTypes ${CONTENT_TYPE}: $contentType"
    }

    private fun getResponseMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val response = exchange.response
        val method = request.method
        val path = request.uri.path
        val statusCode = response.statusCode ?: HttpStatus.CONTINUE
        val contentType = response.headers.contentType
        return "<<<RESPONSE<<< $method $path HTTP${statusCode.value()} ${statusCode.reasonPhrase} ${CONTENT_TYPE}: $contentType"
    }

    override fun filter(swe: ServerWebExchange, wfc: WebFilterChain): Mono<Void> {
        logger.info(getRequestMessage(swe))
        val filter = wfc.filter(swe)
        swe.response.beforeCommit {
            logger.info(getResponseMessage(swe))
            Mono.empty()
        }
        return filter
    }
}