package me.ronan.modules.member

import jakarta.annotation.security.PermitAll
import jakarta.enterprise.context.ApplicationScoped
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext
import me.ronan.infra.auth.JwtTokenProvider
import me.ronan.infra.config.PasswordEncoder
import me.ronan.infra.exception.ExistEmailException
import me.ronan.infra.exception.NoExistEmailException
import me.ronan.infra.exception.ErrorCode
import me.ronan.modules.vo.RequestMemberLogin
import me.ronan.modules.vo.RequestMemberSave
import me.ronan.modules.vo.ResponseMemberSave
import mu.KotlinLogging
import org.eclipse.microprofile.jwt.JsonWebToken

@Path(MemberResource.ROOT)
class MemberResource(
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberService: MemberService,
    private val jwt: JsonWebToken
) {
    companion object {
        const val ROOT = "/api/order/members"
        const val ADD_MEMBER = "/add"
        const val ADD_MEMBER_RESULT = "/{email}/detail"
        const val LOGIN_MEMBER = "login"
        private val log = KotlinLogging.logger { }
    }

    @GET
//    @PermitAll
    @Path(ADD_MEMBER)
    @Produces(MediaType.APPLICATION_JSON)
    fun addMember(@Context ctx: SecurityContext): Response {
        log.info("${getResponseString(ctx)}")
        return Response.ok(ResponseMemberSave("","","")).build()
    }
    private fun getResponseString(ctx: SecurityContext): String {
        val name: String
        if (ctx.userPrincipal == null) {
            name = "anonymous"
        } else if (ctx.userPrincipal.name != jwt.getName()) {
            throw InternalServerErrorException("Principal and JsonWebToken names do not match")
        } else {
            name = ctx.userPrincipal.name
        }
        return String.format(
            "hello + %s,"
                    + " isHttps: %s,"
                    + " authScheme: %s,"
                    + " hasJWT: %s",
            name, ctx.isSecure, ctx.authenticationScheme, hasJwt()
        )
    }

    private fun hasJwt(): Boolean {
        return jwt.getClaimNames() != null
    }

    @POST
    @PermitAll
    @Path(ADD_MEMBER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun addMember(@Valid requestMemberSave: RequestMemberSave): Response {
        val hasAccount = memberService.hasEmail(requestMemberSave.email)
        if (hasAccount) {
            throw ExistEmailException(ErrorCode.EXIST_EMAIL)
        }
        val member = memberService.register(
            requestMemberSave.email,
            passwordEncoder.encode(requestMemberSave.password),
            passwordEncoder.encode(requestMemberSave.simplePassword),
        )
        return Response.ok(jwtTokenProvider.createToken(member)).build()
    }

    @POST
    @PermitAll
    @Path(LOGIN_MEMBER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun login(@Valid requestMemberLogin: RequestMemberLogin): Response {
        val hasAccount = memberService.hasEmail(requestMemberLogin.email)
        if (!hasAccount) {
            throw NoExistEmailException(ErrorCode.NO_EXIST_EMAIL)
        }
        val member = memberService.login(requestMemberLogin)
        return Response.ok(jwtTokenProvider.createToken(member)).build()
    }
}