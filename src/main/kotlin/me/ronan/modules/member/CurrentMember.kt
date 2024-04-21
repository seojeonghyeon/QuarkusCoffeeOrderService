package me.ronan.modules.member

import jakarta.ws.rs.NameBinding
import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*

@NameBinding
@Retention(RUNTIME)
@Target(FIELD, FUNCTION, VALUE_PARAMETER)
annotation class CurrentMember()
