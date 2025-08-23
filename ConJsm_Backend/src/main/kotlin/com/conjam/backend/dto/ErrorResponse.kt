package com.conjam.backend.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "에러 응답")
data class ErrorResponse(
    @Schema(description = "요청 성공 여부", example = "false")
    val success: Boolean = false,
    
    @Schema(description = "에러 코드", example = "DATA_NOT_FOUND")
    val errorCode: String,
    
    @Schema(description = "에러 메시지", example = "요청한 데이터를 찾을 수 없습니다.")
    val message: String,
    
    @Schema(description = "요청 경로", example = "/api/performances/PF001234")
    val path: String? = null,
    
    @Schema(description = "에러 발생 시간", example = "2025-01-15 10:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    
    @Schema(description = "상세 에러 정보")
    val details: Any? = null
)
