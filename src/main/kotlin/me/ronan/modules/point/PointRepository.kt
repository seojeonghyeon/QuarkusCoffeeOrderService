package me.ronan.modules.point

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Parameters
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class PointRepository : PanacheRepository<Point> {
    fun findByMemberIdAndPointId(memberId: String, pointId: Long) : Point? {
        return find("SELECT p " +
                "FROM points p " +
                "WHERE p.member.memberId = :memberId " +
                "AND p.id = :pointId",
        Parameters.with("memberId", memberId).and("pointId", pointId))
            .firstResult()
    }
}
