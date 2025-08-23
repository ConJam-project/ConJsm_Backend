package com.conjam.backend.exception

import org.springframework.http.HttpStatus

/**
 * 애플리케이션 에러 코드 정의
 */
enum class ErrorCode(
    val code: String,
    val message: String,
    val status: HttpStatus
) {
    // 일반적인 에러
    INTERNAL_SERVER_ERROR("E001", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PARAMETER("E002", "잘못된 요청 파라미터입니다.", HttpStatus.BAD_REQUEST),
    
    // 데이터 관련 에러
    DATA_NOT_FOUND("D001", "요청한 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PERFORMANCE_NOT_FOUND("D002", "공연 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    
    // 외부 API 관련 에러
    EXTERNAL_API_ERROR("A001", "외부 서비스 연동 중 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY),
    KOPIS_API_ERROR("A002", "KOPIS API 호출 중 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY),
    KOPIS_API_TIMEOUT("A003", "KOPIS API 응답 시간을 초과했습니다.", HttpStatus.GATEWAY_TIMEOUT),
    KOPIS_API_KEY_INVALID("A004", "KOPIS API 키가 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    
    // 파라미터 검증 에러
    PAGE_PARAMETER_INVALID("P001", "페이지 번호는 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    SIZE_PARAMETER_INVALID("P002", "페이지 크기는 1~100 사이여야 합니다.", HttpStatus.BAD_REQUEST),
    DATE_FORMAT_INVALID("P003", "날짜 형식이 올바르지 않습니다. (yyyyMMdd)", HttpStatus.BAD_REQUEST),
    GENRE_PARAMETER_INVALID("P004", "지원하지 않는 장르입니다.", HttpStatus.BAD_REQUEST),
    PERFORMANCE_ID_INVALID("P005", "공연 ID가 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
    
    companion object {
        /**
         * 코드로 ErrorCode 찾기
         */
        fun findByCode(code: String): ErrorCode? {
            return values().find { it.code == code }
        }
    }
}
