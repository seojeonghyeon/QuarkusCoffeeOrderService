package me.ronan.modules.vo

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RequestMemberLogin (
    val email: String,
    val password: String,
)
