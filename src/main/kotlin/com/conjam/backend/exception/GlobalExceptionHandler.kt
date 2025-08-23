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
        logger.warn("비즈니스 예외 발생 [${ex.errorCode.code}]: ${ex.message}", ex)

        val errorResponse = ErrorResponse(
            errorCode = ex.errorCode.code,
            errorMessage = ex.message ?: ex.errorCode.message,
            userMessage = ex.userMessage,
            path = request.getDescription(false).substringAfter("uri=")
        )

        return ResponseEntity.status(ex.errorCode.status).body(errorResponse)
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

        val errorCode = ErrorCode.KOPIS_API_ERROR
        val errorResponse = ErrorResponse(
            errorCode = errorCode.code,
            errorMessage = "외부 API 호출 중 오류가 발생했습니다. 상태코드: ${ex.statusCode}",
            userMessage = "서비스 연동 중 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
            path = request.getDescription(false).substringAfter("uri="),
            details = mapOf(
                "status" to ex.statusCode.value(),
                "response" to ex.responseBodyAsString.take(200)
            )
        )

        return ResponseEntity.status(errorCode.status).body(errorResponse)
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

        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        val errorResponse = ErrorResponse(
            errorCode = errorCode.code,
            errorMessage = "예상하지 못한 서버 오류가 발생했습니다: ${ex.message}",
            userMessage = "서버 내부 오류가 발생했습니다. 관리자에게 문의해주세요.",
            path = request.getDescription(false).substringAfter("uri=")
        )

        return ResponseEntity.status(errorCode.status).body(errorResponse)
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

        val errorCode = ErrorCode.INVALID_PARAMETER
        val errorResponse = ErrorResponse(
            errorCode = errorCode.code,
            errorMessage = ex.message ?: "잘못된 파라미터입니다.",
            userMessage = "요청 파라미터를 확인해주세요.",
            path = request.getDescription(false).substringAfter("uri=")
        )

        return ResponseEntity.status(errorCode.status).body(errorResponse)
    }
}
