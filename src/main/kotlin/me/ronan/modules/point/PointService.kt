package me.ronan.modules.point

import jakarta.ejb.NoSuchEntityException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import me.ronan.infra.exception.NoSuchPointException
import me.ronan.infra.exception.ErrorCode
import me.ronan.modules.member.Member
import me.ronan.modules.member.MemberService

@ApplicationScoped
@Transactional
class PointService(
    private val pointRepository: PointRepository,
    private val memberService: MemberService,
) {
    fun findPointByMemberId(memberId: String): Int {
        return memberService.findByMemberId(memberId).point
    }

    @Transactional
    fun addPoint(memberId: String, addPoint: Int): Long {
        val member = memberService.findByMemberId(memberId)
        val savePoint = Point.createPoint(
            member = member,
            addPoint = addPoint,
        )
        pointRepository.persist(savePoint)
        return savePoint.id!!
    }

    fun findByMemberIdAndPointId(memberId: String, pointId: Long) : Point {
        return pointRepository.findByMemberIdAndPointId(memberId, pointId)
            ?:throw NoSuchEntityException("No Such Entity : [$memberId], [$pointId]")
    }
}
