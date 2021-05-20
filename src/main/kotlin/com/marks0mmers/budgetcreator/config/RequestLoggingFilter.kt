package com.marks0mmers.budgetcreator.config

import com.marks0mmers.budgetcreator.util.requestMessage
import com.marks0mmers.budgetcreator.util.responseMessage
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
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
    LoggerFactory.getLogger(RequestLoggingFilter::class.java).let { logger ->
        logger.info(swe.requestMessage)
        val filter = wfc.filter(swe)
        swe.response.beforeCommit {
            logger.info(swe.responseMessage)
            Mono.empty()
        }
        filter
    }
})