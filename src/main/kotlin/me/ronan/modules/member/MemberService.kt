package me.ronan.modules.member

import io.vertx.core.eventbus.EventBus
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import me.ronan.infra.config.PasswordEncoder
import me.ronan.infra.exception.LoginException
import me.ronan.infra.exception.NoSuchMemberException
import me.ronan.infra.exception.ErrorCode
import me.ronan.modules.vo.RequestMemberLogin

@ApplicationScoped
class MemberService(
    private val passwordEncoder: PasswordEncoder,
    private val memberRepository: MemberRepository,
    private val bus: EventBus,
) {
    fun hasEmail(email: String) : Boolean {
        return memberRepository.findByEmail(email) != null
    }

    @Transactional
    fun register(email: String, encryptedPwdDto: PasswordDto, encryptedSimplePwdDto: PasswordDto): Member {
        val member = Member.createUserMember(email, encryptedPwdDto, encryptedSimplePwdDto)
        memberRepository.persist(member)
        bus.send("MemberCreatedEvent", member)
        return member
    }

    fun findByMemberId(memberId: String): Member {
        return memberRepository.findByMemberId(memberId)
            ?:throw NoSuchMemberException(ErrorCode.NO_MATCH_USER_ID)
    }
    @Transactional
    fun login(memberRequestMemberLogin: RequestMemberLogin): Member {
        val storedMember = memberRepository.findByEmail(memberRequestMemberLogin.email)
            ?:throw LoginException(ErrorCode.NO_EXIST_EMAIL)
        return storedMember.login(
            password = memberRequestMemberLogin.password,
            passwordEncoder = passwordEncoder,
        )
    }
}
