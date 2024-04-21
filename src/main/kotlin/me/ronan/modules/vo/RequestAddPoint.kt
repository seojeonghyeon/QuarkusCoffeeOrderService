package me.ronan.modules.vo

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RequestAddPoint(
    val currentPoint: Int,
    val addPoint: Int,
)
