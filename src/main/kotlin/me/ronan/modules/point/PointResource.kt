package me.ronan.modules.point

import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.ApplicationScoped
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext
import me.ronan.modules.member.CurrentMember
import me.ronan.modules.member.Member
import me.ronan.modules.vo.RequestAddPoint
import me.ronan.modules.vo.ResponseAddPoint
import me.ronan.modules.vo.ResponsePointResult
import org.eclipse.microprofile.jwt.Claim
import org.eclipse.microprofile.jwt.ClaimValue
import org.eclipse.microprofile.jwt.Claims

@Path(PointResource.ROOT)
class PointResource(
    private val pointService: PointService,
) {
    companion object {
        const val ROOT = "/api/order/points"
        const val ADD = "/add"
        const val RESULT_DETAIL = "/{pointId}/detail"
    }

    @GET
    @Path(ADD)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("USER", "MANAGER", "ADMIN")
    fun points(@Context ctx: SecurityContext): Response {
        val memberId = ctx.userPrincipal.name
        val currentPoint = pointService.findPointByMemberId(memberId)
        return Response.ok(ResponseAddPoint(currentPoint)).build()
    }

    @POST
    @Path(ADD)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("USER", "MANAGER", "ADMIN")
    fun addPoint(@Context ctx: SecurityContext, @Valid requestAddPoint: RequestAddPoint): Response {
        val memberId = ctx.userPrincipal.name
        val currentPoint = pointService.findPointByMemberId(memberId)
        if(Point.validate(requestAddPoint, currentPoint)) {
            val pointId = pointService.addPoint(memberId, requestAddPoint.addPoint)
            return Response.ok(pointId).build()
        }
        return Response.noContent().build()
    }

    @GET
    @Path(RESULT_DETAIL)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("USER", "MANAGER", "ADMIN")
    fun addResultDetail(@Context ctx: SecurityContext, @PathParam(value = "pointId") pointId: Long) : Response {
        val memberId = ctx.userPrincipal.name
        val savePoint = pointService.findByMemberIdAndPointId(memberId, pointId)
        return Response.ok(
            ResponsePointResult.createResponsePointResult(
                member = savePoint.getMember(),
                point = savePoint,
            )
        ).build()
    }
}
