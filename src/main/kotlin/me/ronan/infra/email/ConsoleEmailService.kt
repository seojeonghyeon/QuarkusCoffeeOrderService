package me.ronan.infra.email

import jakarta.enterprise.context.ApplicationScoped
import mu.KotlinLogging


@ApplicationScoped
class ConsoleEmailService : EmailService {
    companion object {
        private val log = KotlinLogging.logger { }
    }
    override fun sendEmail(emailMessage: EmailMessage) {
        log.info{ "sent mail : ${emailMessage.message}" }
    }
}