package com.conjam.backend.service

import com.conjam.backend.client.KopisApiClient
import com.conjam.backend.dto.PerformanceDetailResponse
import com.conjam.backend.dto.PerformanceListResponse
import com.conjam.backend.exception.DataNotFoundException
import com.conjam.backend.exception.InvalidParameterException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PerformanceService(
    private val kopisApiClient: KopisApiClient
) {

    private val logger = LoggerFactory.getLogger(PerformanceService::class.java)

    /**
     * 공연 목록 조회
     * @param page 페이지 번호 (1부터 시작)
     * @param size 페이지 크기 (최대 100)
     * @param genre 장르 (연극, 뮤지컬, 클래식 등)
     * @param area 지역 코드 (11:서울, 26:부산 등)
     * @param startDate 공연 시작일 (yyyyMMdd)
     * @param endDate 공연 종료일 (yyyyMMdd)
     */
    suspend fun getPerformanceList(
        page: Int,
        size: Int,
        genre: String?,
        area: String?,
        startDate: String? = null,
        endDate: String? = null
    ): PerformanceListResponse {

        logger.info("공연 목록 조회 요청 - page: $page, size: $size, genre: $genre, area: $area")

        // 파라미터 검증
        val validPage = if (page < 1) 1 else page
        val validSize = when {
            size < 1 -> 10
            size > 100 -> 100
            else -> size
        }

        return kopisApiClient.getPerformanceList(
            page = validPage,
            size = validSize,
            genre = genre,
            area = area,
            startDate = startDate,
            endDate = endDate
        ).also { response ->
            logger.info("공연 목록 조회 완료 - ${response.performances.size}건")
        }
    }

    /**
     * 공연 상세 조회
     * @param performanceId 공연 ID (mt20id)
     */
    suspend fun getPerformanceDetail(performanceId: String): PerformanceDetailResponse {

        logger.info("공연 상세 조회 요청 - ID: $performanceId")

        // 파라미터 검증
        if (performanceId.isBlank()) {
            throw InvalidParameterException("공연 ID가 비어있습니다.")
        }

        return kopisApiClient.getPerformanceDetail(performanceId).also { response ->
            if (response.performance == null) {
                throw DataNotFoundException("공연 정보를 찾을 수 없습니다. ID: $performanceId")
            }
            logger.info("공연 상세 조회 완료 - ${response.performance.title}")
        }
    }
}