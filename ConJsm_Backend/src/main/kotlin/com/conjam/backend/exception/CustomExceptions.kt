package com.conjam.backend.exception

/**
 * 기본 비즈니스 예외
 */
abstract class BusinessException(
    message: String,
    val errorCode: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)

/**
 * 외부 API 호출 실패 예외
 */
class ExternalApiException(
    message: String,
    errorCode: String = "EXTERNAL_API_ERROR",
    cause: Throwable? = null
) : BusinessException(message, errorCode, cause)

/**
 * 데이터를 찾을 수 없는 예외
 */
class DataNotFoundException(
    message: String,
    errorCode: String = "DATA_NOT_FOUND",
    cause: Throwable? = null
) : BusinessException(message, errorCode, cause)

/**
 * 잘못된 파라미터 예외
 */
class InvalidParameterException(
    message: String,
    errorCode: String = "INVALID_PARAMETER",
    cause: Throwable? = null
) : BusinessException(message, errorCode, cause)
