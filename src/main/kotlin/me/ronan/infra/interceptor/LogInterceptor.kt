package me.ronan.infra.interceptor

import jakarta.annotation.Priority
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.container.ContainerResponseFilter
import jakarta.ws.rs.ext.Provider
import mu.KotlinLogging
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.io.IOException
import java.util.*

@Provider
@Priority(1000)
class LogInterceptor : ContainerRequestFilter, ContainerResponseFilter {
    companion object {
        private val log = KotlinLogging.logger {}
        private const val LOG_ID = "logId"
    }

    @ConfigProperty(name = "app.logging.enabled", defaultValue = "true")
    lateinit var loggingEnabled: String

    @Throws(IOException::class)
    override fun filter(requestContext: ContainerRequestContext) {
        if (!loggingEnabled.toBoolean()){
            return
        }
        val requestUri = requestContext.uriInfo.requestUri.toString()
        val uuid = UUID.randomUUID().toString()
        requestContext.setProperty(LOG_ID, uuid)
        log.info("REQUEST [${uuid}][${requestUri}]")
    }

    @Throws(IOException::class)
    override fun filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext) {
        if(!loggingEnabled.toBoolean()){
            return
        }
        val logId = requestContext.getProperty(LOG_ID)
        val requestUri = requestContext.uriInfo.requestUri
        log.info("RESPONSE [$logId][$requestUri][${responseContext.status}]")
    }
}
