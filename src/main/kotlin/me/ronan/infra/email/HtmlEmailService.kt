package me.ronan.infra.email

import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import mu.KotlinLogging


@ApplicationScoped
class HtmlEmailService: EmailService {
    companion object {
        private val log = KotlinLogging.logger { }
    }

    @Inject
    private lateinit var mailer: Mailer

    override fun sendEmail(emailMessage: EmailMessage) {
        try {
            mailer.send(
                Mail.withText(
                    emailMessage.to,
                    emailMessage.subject,
                    emailMessage.message,
                )
            )
            log.info { "sent mail: ${emailMessage.message}" }
        }catch (exception : RuntimeException) {
            throw RuntimeException("failed to send mail : $exception")
        }
    }
}