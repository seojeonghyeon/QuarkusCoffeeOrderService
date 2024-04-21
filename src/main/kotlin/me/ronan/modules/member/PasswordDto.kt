package me.ronan.modules.member

data class PasswordDto(
    val password: String,
    val passwordSalt: String,
)