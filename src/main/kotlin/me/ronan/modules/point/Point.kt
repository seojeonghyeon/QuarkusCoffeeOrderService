package me.ronan.modules.point

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.*
import me.ronan.infra.exception.AlreadyCompletedPointException
import me.ronan.infra.exception.AlreadyNotPendingPointException
import me.ronan.infra.exception.NoSuchPointException
import me.ronan.infra.exception.TooMuchAddPointException
import me.ronan.infra.exception.ErrorCode
import me.ronan.modules.member.Member
import me.ronan.modules.vo.RequestAddPoint
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@Entity(name = "points")
data class Point protected constructor(
    @Enumerated(EnumType.STRING)
    var status: PointStatus,
    var currentPoint: Int,
    var addPoint: Int,
) :PanacheEntity() {
    @ManyToOne
    @JoinColumn(name = "memberId")
    private var member: Member? = null

    @CreationTimestamp
    val createdDate: ZonedDateTime? = null
    @UpdateTimestamp
    val updatedDate: ZonedDateTime? = null

    companion object {
        const val MIN_POINT_LIMIT = 1;
        const val MAX_POINT_LIMIT = 200_000

        fun createPoint(
            member: Member,
            addPoint: Int
        ): Point {
            val point = Point(
                currentPoint = member.point,
                addPoint = addPoint,
                status = PointStatus.PENDING,
            )
            point.setMember(member)
            point.inducePoint()
            return point
        }

        fun validate(requestAddPoint: RequestAddPoint, storedPoint: Int): Boolean {
            if(requestAddPoint.currentPoint != storedPoint
                && requestAddPoint.addPoint !in MIN_POINT_LIMIT..MAX_POINT_LIMIT) {
                throw NoSuchPointException(ErrorCode.NO_SUCH_POINT)
            }
            return true
        }
    }

    fun setMember(member: Member) {
        this.member = member
        member.getPoints().add(this)
    }

    fun getMember() : Member {
        return this.member!!
    }

    fun inducePoint(): Unit {
        if(status != PointStatus.PENDING) {
            throw AlreadyNotPendingPointException(ErrorCode.ALREADY_NOT_PENDING_POINT)
        }
        status = if(addPoint in MIN_POINT_LIMIT..MAX_POINT_LIMIT) PointStatus.CONFIRMED else PointStatus.REJECTED
        if (status == PointStatus.CONFIRMED) {
            member!!.inducePoint(addPoint)
        } else if (status == PointStatus.REJECTED) {
            throw TooMuchAddPointException(ErrorCode.TOO_MUCH_ADD_POINT)
        }
    }

    fun cancel(): Unit {
        if (status == PointStatus.COMPLETED) {
            throw AlreadyCompletedPointException(ErrorCode.ALREADY_COMPLETED_POINT)
        }
        status = PointStatus.CANCELED
        member!!.deductPoint(addPoint)
    }
}
