package me.ronan.modules.event

import io.quarkus.qute.Location
import io.quarkus.qute.Template
import io.quarkus.vertx.ConsumeEvent
import jakarta.enterprise.context.ApplicationScoped
import me.ronan.infra.email.EmailMessage
import me.ronan.infra.email.EmailService
import me.ronan.modules.member.Member
import mu.KotlinLogging
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class MemberEventListener(
    @Location("member-created-result.html")
    private val memberCreatedResult: Template,
) {
    companion object {
        private val log = KotlinLogging.logger { }
        private const val ROOT = "/api/order/members"
    }

    @ConfigProperty(name = "app.host")
    private lateinit var host: String

    @ConfigProperty(name = "email-service.implementation-class")
    private lateinit var implementationClass: String

    @ConsumeEvent(value = "MemberCreatedEvent")
    fun handleMemberCreatedEvent(member: Member) {
        val emailSubject = "[CoffeeOrderService] 계정 생성"
        val contextMessage = "가입을 축하합니다."
        sendMemberCreatedEmail(member, emailSubject, contextMessage)
    }

    private fun sendMemberCreatedEmail(member: Member, emailSubject: String, contextMessage: String) {
        val emailService = Class.forName(implementationClass).newInstance() as EmailService
        memberCreatedResult.data("name", member.memberName)
        memberCreatedResult.data("message", contextMessage)
        memberCreatedResult.data("host", host + ROOT + member.email + "/detail")
        val message = memberCreatedResult.toString()
        val emailMessage = EmailMessage(
            subject = emailSubject,
            to = member.email,
            message = message,
        )
        emailService.sendEmail(emailMessage)
        log.info("[${member.memberId}][${member.memberName}] Mail sent to ${member.email}")
    }
}
