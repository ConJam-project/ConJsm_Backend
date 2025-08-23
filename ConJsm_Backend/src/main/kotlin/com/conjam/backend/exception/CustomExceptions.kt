package com.conjam.backend.exception

/**
 * 기본 비즈니스 예외
 */
abstract class BusinessException(
    message: String,
    val errorCode: ErrorCode,
    val userMessage: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)

/**
 * 외부 API 호출 실패 예외
 */
class ExternalApiException(
    message: String,
    errorCode: ErrorCode = ErrorCode.EXTERNAL_API_ERROR,
    userMessage: String? = null,
    cause: Throwable? = null
) : BusinessException(message, errorCode, userMessage, cause)

/**
 * KOPIS API 호출 실패 예외
 */
class KopisApiException(
    message: String,
    errorCode: ErrorCode = ErrorCode.KOPIS_API_ERROR,
    userMessage: String? = null,
    cause: Throwable? = null
) : BusinessException(message, errorCode, userMessage, cause)

/**
 * 데이터를 찾을 수 없는 예외
 */
class DataNotFoundException(
    message: String,
    errorCode: ErrorCode = ErrorCode.DATA_NOT_FOUND,
    userMessage: String? = null,
    cause: Throwable? = null
) : BusinessException(message, errorCode, userMessage, cause)

/**
 * 공연 정보를 찾을 수 없는 예외
 */
class PerformanceNotFoundException(
    message: String,
    errorCode: ErrorCode = ErrorCode.PERFORMANCE_NOT_FOUND,
    userMessage: String? = "요청하신 공연 정보를 찾을 수 없습니다.",
    cause: Throwable? = null
) : BusinessException(message, errorCode, userMessage, cause)

/**
 * 잘못된 파라미터 예외
 */
class InvalidParameterException(
    message: String,
    errorCode: ErrorCode = ErrorCode.INVALID_PARAMETER,
    userMessage: String? = null,
    cause: Throwable? = null
) : BusinessException(message, errorCode, userMessage, cause)
