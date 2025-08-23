package com.conjam.backend.exception

import com.conjam.backend.dto.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.reactive.function.client.WebClientResponseException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    /**
     * 비즈니스 예외 처리
     */
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(
        ex: BusinessException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("비즈니스 예외 발생: ${ex.message}", ex)

        val errorResponse = ErrorResponse(
            errorCode = ex.errorCode,
            message = ex.message ?: "비즈니스 로직 오류가 발생했습니다.",
            path = request.getDescription(false).substringAfter("uri=")
        )

        val status = when (ex) {
            is DataNotFoundException -> HttpStatus.NOT_FOUND
            is InvalidParameterException -> HttpStatus.BAD_REQUEST
            is ExternalApiException -> HttpStatus.BAD_GATEWAY
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity.status(status).body(errorResponse)
    }

    /**
     * WebClient 예외 처리 (외부 API 호출 오류)
     */
    @ExceptionHandler(WebClientResponseException::class)
    fun handleWebClientResponseException(
        ex: WebClientResponseException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error("외부 API 호출 오류: ${ex.statusCode} - ${ex.responseBodyAsString}", ex)

        val errorResponse = ErrorResponse(
            errorCode = "EXTERNAL_API_ERROR",
            message = "외부 서비스 연동 중 오류가 발생했습니다.",
            path = request.getDescription(false).substringAfter("uri="),
            details = mapOf(
                "status" to ex.statusCode.value(),
                "response" to ex.responseBodyAsString
            )
        )

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse)
    }

    /**
     * 일반적인 예외 처리 (예상하지 못한 오류)
     */
    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error("예상하지 못한 오류 발생", ex)

        val errorResponse = ErrorResponse(
            errorCode = "INTERNAL_SERVER_ERROR",
            message = "서버 내부 오류가 발생했습니다.",
            path = request.getDescription(false).substringAfter("uri=")
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    /**
     * 잘못된 파라미터 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        ex: IllegalArgumentException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("잘못된 파라미터: ${ex.message}", ex)

        val errorResponse = ErrorResponse(
            errorCode = "INVALID_PARAMETER",
            message = ex.message ?: "잘못된 파라미터입니다.",
            path = request.getDescription(false).substringAfter("uri=")
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }
}
