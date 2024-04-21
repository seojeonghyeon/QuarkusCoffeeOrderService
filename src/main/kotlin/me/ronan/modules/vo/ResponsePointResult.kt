package me.ronan.modules.vo

import me.ronan.modules.member.Member
import me.ronan.modules.point.Point
import me.ronan.modules.point.PointStatus
import java.time.ZonedDateTime

class ResponsePointResult protected constructor(
    val memberName: String,
    val beforePoint: Int,
    val afterPoint: Int,
    val status: PointStatus,
    val createdDate: ZonedDateTime,
    val updatedDate: ZonedDateTime,
){
    companion object {
        fun createResponsePointResult(
            member: Member,
            point: Point,
        ): ResponsePointResult {
            return ResponsePointResult(
                memberName = member.memberName,
                beforePoint = point.currentPoint,
                afterPoint = point.currentPoint + point.addPoint,
                status = point.status,
                createdDate = point.createdDate!!,
                updatedDate = point.updatedDate!!,
            )
        }
    }
}
