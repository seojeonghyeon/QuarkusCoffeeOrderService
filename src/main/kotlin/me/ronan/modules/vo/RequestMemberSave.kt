package me.ronan.modules.vo

import com.fasterxml.jackson.annotation.JsonInclude


@JsonInclude(JsonInclude.Include.NON_NULL)
data class RequestMemberSave(
    val email: String,
    val password: String,
    val simplePassword: String,
)
