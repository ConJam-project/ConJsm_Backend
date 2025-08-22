package com.conjam.backend.controller

import com.conjam.backend.dto.PerformanceDetailResponse
import com.conjam.backend.dto.PerformanceListResponse
import com.conjam.backend.service.PerformanceService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/performances")
class PerformanceController(
    private val performanceService: PerformanceService
) {

    private val logger = LoggerFactory.getLogger(PerformanceController::class.java)

    /**
     * 공연 목록 조회
     * @param page 페이지 번호 (기본값: 1)
     * @param size 페이지 크기 (기본값: 10, 최대: 100)
     * @param genre 장르 (연극, 뮤지컬, 클래식, 국악, 대중음악, 무용, 복합, 서커스/마술)
     * @param area 지역 코드 (11:서울, 26:부산, 27:대구, 28:인천 등)
     * @param startDate 공연 시작일 (yyyyMMdd 형식, 예: 20250101)
     * @param endDate 공연 종료일 (yyyyMMdd 형식, 예: 20250131)
     */
    @GetMapping
    fun getPerformances(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) genre: String?,
        @RequestParam(required = false) area: String?,
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?
    ): ResponseEntity<PerformanceListResponse> {

        logger.info("공연 목록 조회 API 호출 - page: $page, size: $size, genre: $genre, area: $area")

        return try {
            val response = performanceService.getPerformanceList(
                page = page,
                size = size,
                genre = genre,
                area = area,
                startDate = startDate,
                endDate = endDate
            )

            ResponseEntity.ok(response)

        } catch (e: Exception) {
            logger.error("공연 목록 조회 API 오류", e)
            ResponseEntity.internalServerError().body(PerformanceListResponse())
        }
    }

    /**
     * 특정 공연 상세 정보 조회
     * @param performanceId 공연 ID (KOPIS mt20id)
     */
    @GetMapping("/{performanceId}")
    fun getPerformanceDetail(
        @PathVariable performanceId: String
    ): ResponseEntity<PerformanceDetailResponse> {

        logger.info("공연 상세 조회 API 호출 - ID: $performanceId")

        return try {
            val response = performanceService.getPerformanceDetail(performanceId)

            if (response.performance != null) {
                ResponseEntity.ok(response)
            } else {
                logger.warn("공연 정보를 찾을 수 없음 - ID: $performanceId")
                ResponseEntity.notFound().build()
            }

        } catch (e: Exception) {
            logger.error("공연 상세 조회 API 오류", e)
            ResponseEntity.internalServerError().body(PerformanceDetailResponse())
        }
    }

    /**
     * API 상태 확인용 헬스체크 엔드포인트
     */
    @GetMapping("/health")
    fun healthCheck(): Map<String, Any> {
        return mapOf(
            "status" to "UP",
            "service" to "ConJam Performance API",
            "timestamp" to System.currentTimeMillis(),
            "description" to "KOPIS 공연예술통합전산망 연동 API"
        )
    }
}