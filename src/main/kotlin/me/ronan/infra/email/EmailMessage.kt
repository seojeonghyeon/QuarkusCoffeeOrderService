package me.ronan.infra.email

data class EmailMessage(
    val to: String,
    val subject: String,
    val message: String,
)
