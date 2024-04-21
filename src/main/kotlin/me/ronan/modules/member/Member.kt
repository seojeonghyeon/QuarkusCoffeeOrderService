package me.ronan.modules.member

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.*
import me.ronan.infra.config.PasswordEncoder
import me.ronan.infra.exception.LoginException
import me.ronan.infra.exception.ErrorCode
import me.ronan.modules.authority.Authority
import me.ronan.modules.point.Point
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.HashSet

@Entity(name = "members")
data class Member protected constructor(
    @Id
    @Column(name = "memberId")
    var memberId: String? = null,
    val memberName: String,
    @Column(unique = true)
    val email: String,
    val password: String,
    val passwordSalt: String,
    val simplePassword: String,
    val simplePasswordSalt: String,
    var point: Int,
    var disabled: Boolean,
) : PanacheEntityBase {
    @CreationTimestamp
    private val createdDate: ZonedDateTime? = null
    @UpdateTimestamp
    private val updatedDate: ZonedDateTime? = null
    @ManyToMany(mappedBy = "members", cascade = [CascadeType.ALL])
    private val authorities: MutableSet<Authority> = HashSet()
    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private val points: MutableList<Point> = ArrayList()
//    @JsonIgnore
//    @OneToMany(mappedBy = "member")
//    private val orders: MutableList<Order> = ArrayList()

    private val isNew: Boolean
        get() = memberId == null

    fun setAuthorities(authority: Authority): Unit {
        this.authorities.add(authority)
        authority.getMembers().add(this)
    }

    fun getAuthorities(): MutableSet<Authority> {
        return authorities
    }

    fun getPoints(): MutableList<Point> {
        return points
    }

    fun inducePoint(point: Int): Int {
        this.point += point
        return this.point
    }

    fun deductPoint(point: Int): Unit {
        this.point -= point
    }

    fun login(
        password: String,
        passwordEncoder: PasswordEncoder
    ): Member {
        if (!isLoginSuccess(password, passwordEncoder)) {
            throw LoginException(ErrorCode.LOGIN_FAIL)
        }
        return this
    }

    private fun isLoginSuccess(
        password: String,
        passwordEncoder: PasswordEncoder
    ): Boolean {
        return passwordEncoder.matches(
            password,
            PasswordDto(
                password = this.password,
                passwordSalt = this.passwordSalt,
            )
        )
    }

    companion object {
        fun createUserMember(
            email: String,
            encryptedPwdDto: PasswordDto,
            encryptedSimplePwdDto: PasswordDto,
        ): Member{
            val authority: Authority = Authority.createAuthority("USER")
            return createMember(
                authority = authority,
                email = email,
                encryptedPwdDto = encryptedPwdDto,
                encryptedSimplePwdDto = encryptedSimplePwdDto,
            )
        }

        fun createManagerMember(
            email: String,
            encryptedPwdDto: PasswordDto,
            encryptedSimplePwdDto: PasswordDto,
        ): Member{
            val authority: Authority = Authority.createAuthority("MANAGER")
            return createMember(
                authority = authority,
                email = email,
                encryptedPwdDto = encryptedPwdDto,
                encryptedSimplePwdDto = encryptedSimplePwdDto,
            )
        }

        fun createAdminMember(
            email: String,
            encryptedPwdDto: PasswordDto,
            encryptedSimplePwdDto: PasswordDto,
        ): Member{
            val authority: Authority = Authority.createAuthority("ADMIN")
            return createMember(
                authority = authority,
                email = email,
                encryptedPwdDto = encryptedPwdDto,
                encryptedSimplePwdDto = encryptedSimplePwdDto,
            )
        }

        private fun createMember(
            authority: Authority,
            email: String,
            encryptedPwdDto: PasswordDto,
            encryptedSimplePwdDto: PasswordDto,
        ): Member {
            val member = Member(
                UUID.randomUUID().toString(),
                AdjectiveWord.getWordOne()+" "+AnimalWord.getWordOne(),
                email,
                encryptedPwdDto.password,
                encryptedPwdDto.passwordSalt,
                encryptedSimplePwdDto.password,
                encryptedSimplePwdDto.passwordSalt,
                0,
                false
            )
            member.setAuthorities(authority)
            authority.setMember(member)
            return member
        }
    }

}