package me.ronan.infra.email

interface EmailService {
    fun sendEmail(emailMessage: EmailMessage)
}