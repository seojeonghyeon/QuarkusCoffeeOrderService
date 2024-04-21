package me.ronan.infra.exception

import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import me.ronan.infra.exception.ExistEmailException
import me.ronan.infra.exception.LoginException
import me.ronan.infra.exception.NoExistEmailException

@Provider
class ExistEmailExceptionHandler : ExceptionMapper<ExistEmailException> {
    override fun toResponse(exception: ExistEmailException): Response {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(exception.errorCode.description)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .build()
    }
}

@Provider
class NoExistEmailExceptionHandler : ExceptionMapper<NoExistEmailException> {
    override fun toResponse(exception: NoExistEmailException): Response {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(exception.errorCode.description)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .build()
    }
}

@Provider
class LoginExceptionHandler : ExceptionMapper<LoginException> {
    override fun toResponse(exception: LoginException): Response {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(exception.errorCode.description)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .build()
    }
}