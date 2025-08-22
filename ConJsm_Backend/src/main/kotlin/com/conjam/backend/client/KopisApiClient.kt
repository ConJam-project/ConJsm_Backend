package com.conjam.backend.client

import com.conjam.backend.dto.PerformanceDetailResponse
import com.conjam.backend.dto.PerformanceListResponse
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class KopisApiClient(
    private val webClient: WebClient.Builder
) {

    private val logger = LoggerFactory.getLogger(KopisApiClient::class.java)
    private val xmlMapper = XmlMapper()

    @Value("\${kopis.api.key}")
    private lateinit var apiKey: String

    @Value("\${kopis.api.base-url}")
    private lateinit var baseUrl: String

    private val client by lazy {
        webClient
            .baseUrl(baseUrl)
            .defaultHeader("Accept", "application/xml")
            .build()
    }

    /**
     * 공연 목록 조회
     * KOPIS API 사용 시 필수 파라미터: service, stdate, eddate, cpage, rows
     */
    fun getPerformanceList(
        page: Int,
        size: Int,
        genre: String?,
        area: String?,
        startDate: String? = null,
        endDate: String? = null
    ): PerformanceListResponse {

        try {
            // 날짜가 없으면 현재 날짜 기준으로 설정 (최근 1개월)
            val today = LocalDate.now()
            val defaultStartDate = startDate ?: today.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            val defaultEndDate = endDate ?: today.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

            val uri = buildString {
                append("/pblprfr")
                append("?service=$apiKey")
                append("&stdate=$defaultStartDate")
                append("&eddate=$defaultEndDate")
                append("&cpage=$page")
                append("&rows=${if (size > 100) 100 else size}") // 최대 100건

                // 선택적 파라미터들
                genre?.let {
                    when(it.lowercase()) {
                        "연극" -> append("&shcate=AAAA")
                        "뮤지컬" -> append("&shcate=GGGA")
                        "클래식" -> append("&shcate=CCCA")
                        "국악" -> append("&shcate=CCCC")
                        "대중음악" -> append("&shcate=CCCD")
                        "무용" -> append("&shcate=BBBC")
                        "복합" -> append("&shcate=EEEA")
                        "서커스/마술" -> append("&shcate=EEEB")
                        else -> append("&shcate=$it")
                    }
                }
                area?.let { append("&signgucode=$it") }
            }

            logger.info("KOPIS API 호출: $baseUrl$uri")

            val xmlResponse = client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String::class.java)
                .block()

            logger.info("KOPIS API XML 응답 길이: ${xmlResponse?.length}")
            logger.debug("KOPIS API XML 응답: $xmlResponse")

            val response = if (!xmlResponse.isNullOrBlank()) {
                try {
                    xmlMapper.readValue(xmlResponse, PerformanceListResponse::class.java)
                } catch (e: Exception) {
                    logger.error("XML 파싱 오류", e)
                    PerformanceListResponse()
                }
            } else {
                PerformanceListResponse()
            }

            logger.info("KOPIS API 응답: ${response.performances.size}건의 공연 정보")
            return response

        } catch (e: WebClientResponseException) {
            logger.error("KOPIS API 호출 오류: ${e.statusCode} - ${e.responseBodyAsString}")
            return PerformanceListResponse()
        } catch (e: Exception) {
            logger.error("KOPIS API 호출 중 예외 발생", e)
            return PerformanceListResponse()
        }
    }

    /**
     * 공연 상세 조회
     */
    fun getPerformanceDetail(performanceId: String): PerformanceDetailResponse {
        try {
            val uri = "/pblprfr/$performanceId?service=$apiKey"

            logger.info("KOPIS API 상세 조회: $baseUrl$uri")

            val xmlResponse = client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String::class.java)
                .block()

            logger.info("KOPIS API 상세 XML 응답 길이: ${xmlResponse?.length}")
            logger.debug("KOPIS API 상세 XML 응답: $xmlResponse")

            val response = if (!xmlResponse.isNullOrBlank()) {
                try {
                    xmlMapper.readValue(xmlResponse, PerformanceDetailResponse::class.java)
                } catch (e: Exception) {
                    logger.error("XML 파싱 오류", e)
                    PerformanceDetailResponse()
                }
            } else {
                PerformanceDetailResponse()
            }

            logger.info("KOPIS API 상세 응답: ${response.performance?.title}")
            return response

        } catch (e: WebClientResponseException) {
            logger.error("KOPIS API 상세 조회 오류: ${e.statusCode} - ${e.responseBodyAsString}")
            return PerformanceDetailResponse()
        } catch (e: Exception) {
            logger.error("KOPIS API 상세 조회 중 예외 발생", e)
            return PerformanceDetailResponse()
        }
    }
}