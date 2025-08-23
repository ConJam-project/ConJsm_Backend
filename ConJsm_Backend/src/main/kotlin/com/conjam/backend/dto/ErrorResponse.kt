package com.conjam.backend.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "에러 응답")
data class ErrorResponse(
    @Schema(description = "요청 성공 여부", example = "false")
    val success: Boolean = false,
    
    @Schema(description = "에러 코드", example = "D001")
    val errorCode: String,
    
    @Schema(description = "에러 메시지", example = "요청한 데이터를 찾을 수 없습니다.")
    val errorMessage: String,
    
    @Schema(description = "사용자용 메시지", example = "공연 정보를 찾을 수 없습니다.")
    val userMessage: String? = null,
    
    @Schema(description = "요청 경로", example = "/api/performances/PF001234")
    val path: String? = null,
    
    @Schema(description = "에러 발생 시간", example = "2025-01-15 10:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    
    @Schema(description = "상세 에러 정보")
    val details: Any? = null
)

@Schema(description = "헬스체크 응답")
data class HealthCheckResponse(
    @Schema(description = "서비스 상태", example = "UP")
    val status: String,
    
    @Schema(description = "서비스명", example = "ConJam Performance API")
    val service: String,
    
    @Schema(description = "응답 시간", example = "1705375800000")
    val timestamp: Long,
    
    @Schema(description = "서비스 설명", example = "KOPIS 공연예술통합전산망 연동 API")
    val description: String
)

@Schema(description = "API 설정 정보 응답")
data class ApiConfigResponse(
    @Schema(description = "API 키 설정 여부", example = "true")
    val apiKeyConfigured: Boolean,
    
    @Schema(description = "KOPIS API URL", example = "http://www.kopis.or.kr/openApi/restful/pblprfr")
    val baseUrl: String,
    
    @Schema(description = "API 키 앞부분 (마스킹)", example = "abcd1234****")
    val maskedApiKey: String?
)
