package com.conjam.backend.controller

import com.conjam.backend.dto.ApiConfigResponse
import com.conjam.backend.dto.ErrorResponse
import com.conjam.backend.dto.HealthCheckResponse
import com.conjam.backend.dto.PerformanceDetailResponse
import com.conjam.backend.dto.PerformanceListResponse
import com.conjam.backend.service.PerformanceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/performances")
@Tag(name = "Performance API", description = "KOPIS 공연예술통합전산망 연동 공연 정보 API")
class PerformanceController(
    private val performanceService: PerformanceService
) {

    private val logger = LoggerFactory.getLogger(PerformanceController::class.java)

    @Operation(
        summary = "공연 목록 조회",
        description = "KOPIS API를 통해 공연 목록을 조회합니다. 페이지네이션, 장르 필터링, 지역 필터링, 날짜 범위 설정이 가능합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "공연 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PerformanceListResponse::class))]
            ),
            ApiResponse(
                responseCode = "400", 
                description = "잘못된 요청 파라미터",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "502", 
                description = "외부 API 호출 실패",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping
    suspend fun getPerformances(
        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
        @RequestParam(defaultValue = "1") page: Int,
        
        @Parameter(description = "페이지 크기 (최대 100)", example = "10")
        @RequestParam(defaultValue = "10") size: Int,
        
        @Parameter(description = "장르 (연극, 뮤지컬, 클래식, 국악, 대중음악, 무용, 복합, 서커스/마술)", example = "뮤지컬")
        @RequestParam(required = false) genre: String?,
        
        @Parameter(description = "지역 코드 (11:서울, 26:부산, 27:대구, 28:인천 등)", example = "11")
        @RequestParam(required = false) area: String?,
        
        @Parameter(description = "공연 시작일 (yyyyMMdd 형식)", example = "20250101")
        @RequestParam(required = false) startDate: String?,
        
        @Parameter(description = "공연 종료일 (yyyyMMdd 형식)", example = "20250131")
        @RequestParam(required = false) endDate: String?
    ): ResponseEntity<PerformanceListResponse> {

        logger.info("공연 목록 조회 API 호출 - page: $page, size: $size, genre: $genre, area: $area")

        val response = performanceService.getPerformanceList(
            page = page,
            size = size,
            genre = genre,
            area = area,
            startDate = startDate,
            endDate = endDate
        )

        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "공연 상세 정보 조회",
        description = "특정 공연의 상세 정보를 KOPIS API를 통해 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "공연 상세 정보 조회 성공",
                content = [Content(schema = Schema(implementation = PerformanceDetailResponse::class))]
            ),
            ApiResponse(
                responseCode = "404", 
                description = "공연 정보를 찾을 수 없음",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "502", 
                description = "외부 API 호출 실패",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/{performanceId}")
    suspend fun getPerformanceDetail(
        @Parameter(description = "공연 ID (KOPIS mt20id)", example = "PF001234", required = true)
        @PathVariable performanceId: String
    ): ResponseEntity<PerformanceDetailResponse> {

        logger.info("공연 상세 조회 API 호출 - ID: $performanceId")

        val response = performanceService.getPerformanceDetail(performanceId)
        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "API 상태 확인",
        description = "API 서버의 상태를 확인하는 헬스체크 엔드포인트입니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "API 서버 정상 상태",
        content = [Content(schema = Schema(implementation = HealthCheckResponse::class))]
    )
    @GetMapping("/health")
    fun healthCheck(): HealthCheckResponse {
        return HealthCheckResponse(
            status = "UP",
            service = "ConJam Performance API",
            timestamp = System.currentTimeMillis(),
            description = "KOPIS 공연예술통합전산망 연동 API"
        )
    }

    @Operation(
        summary = "KOPIS API 설정 확인",
        description = "KOPIS API 키와 URL 설정을 확인하는 디버깅용 엔드포인트입니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "API 설정 정보 조회 성공",
        content = [Content(schema = Schema(implementation = ApiConfigResponse::class))]
    )
    @GetMapping("/debug/config")
    fun debugConfig(): ApiConfigResponse {
        return performanceService.getApiConfig()
    }
}