package com.conjam.backend.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

/**
 * 에러 응답 DTO
 */
data class ErrorResponse(
    val success: Boolean = false,
    val errorCode: String,
    val message: String,
    val path: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val details: Any? = null
)
