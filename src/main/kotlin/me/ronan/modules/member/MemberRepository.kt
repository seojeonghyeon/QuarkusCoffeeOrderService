package me.ronan.modules.member

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Parameters
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class MemberRepository : PanacheRepository<Member> {
    fun findByEmailOrMemberId(emailOrMemberId: String): Member? {
        return find(
            "SELECT m " +
                    "FROM members m " +
                    "WHERE m.memberId = :emailOrMemberId " +
                    "OR m.email = :emailOrMemberId ",
            Parameters.with("emailOrMemberId", emailOrMemberId))
            .firstResult()
    }

    fun findByMemberId(memberId: String): Member? {
        return find(
            "SELECT m " +
                    "FROM members m " +
                    "WHERE m.memberId = :memberId ",
            Parameters.with("memberId", memberId))
            .firstResult()
    }

    fun findByEmail(email: String) : Member? {
        return find("SELECT m " +
                "FROM members m " +
                "WHERE m.email = :email ",
            Parameters.with("email", email))
            .firstResult()
    }
}