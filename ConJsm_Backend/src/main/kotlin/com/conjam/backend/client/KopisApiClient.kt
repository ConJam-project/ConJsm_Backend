package com.conjam.backend.client

import com.conjam.backend.dto.PerformanceDetailResponse
import com.conjam.backend.dto.PerformanceListResponse
import com.conjam.backend.exception.ExternalApiException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBody
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class KopisApiClient(
    private val webClient: WebClient.Builder
) {

    private val logger = LoggerFactory.getLogger(KopisApiClient::class.java)

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
    suspend fun getPerformanceList(
        page: Int,
        size: Int,
        genre: String?,
        area: String?,
        startDate: String? = null,
        endDate: String? = null
    ): PerformanceListResponse {

        return try {
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

            // 먼저 원시 응답을 문자열로 받아서 로깅
            val rawResponse = client.get()
                .uri(uri)
                .retrieve()
                .awaitBody<String>()

            logger.info("KOPIS API 원시 응답: $rawResponse")

            // 실제 응답이 비어있거나 에러인지 확인
            if (rawResponse.isBlank()) {
                throw ExternalApiException("KOPIS API 응답이 비어있습니다")
            }

            // 에러 응답인지 확인 (KOPIS API는 에러도 XML로 반환)
            if (rawResponse.contains("<error>") || rawResponse.contains("ERROR")) {
                throw ExternalApiException("KOPIS API 에러 응답: $rawResponse")
            }

            // XML 파싱 시도 - ObjectMapper를 직접 사용
            val objectMapper = com.fasterxml.jackson.dataformat.xml.XmlMapper()
            val response = try {
                objectMapper.readValue(rawResponse, PerformanceListResponse::class.java)
            } catch (e: Exception) {
                logger.error("XML 파싱 실패. 원시 응답: $rawResponse", e)
                throw ExternalApiException("XML 파싱 실패: ${e.message}. 원시 응답: $rawResponse", cause = e)
            }

            logger.info("KOPIS API 응답: ${response.performances.size}건의 공연 정보")
            response

        } catch (e: WebClientResponseException) {
            logger.error("KOPIS API 호출 오류: ${e.statusCode} - ${e.responseBodyAsString}")
            throw ExternalApiException("KOPIS API 호출 실패: ${e.statusCode}", cause = e)
        } catch (e: Exception) {
            logger.error("KOPIS API 호출 중 예외 발생: ${e.message}", e)
            throw ExternalApiException("KOPIS API 호출 중 예외 발생: ${e.message}", cause = e)
        }
    }

    /**
     * 공연 상세 조회
     */
    suspend fun getPerformanceDetail(performanceId: String): PerformanceDetailResponse {
        return try {
            val uri = "/pblprfr/$performanceId?service=$apiKey"

            logger.info("KOPIS API 상세 조회: $baseUrl$uri")

            val response = client.get()
                .uri(uri)
                .retrieve()
                .awaitBody<PerformanceDetailResponse>()

            logger.info("KOPIS API 상세 응답: ${response.performance?.title}")
            response

        } catch (e: WebClientResponseException) {
            logger.error("KOPIS API 상세 조회 오류: ${e.statusCode} - ${e.responseBodyAsString}")
            throw ExternalApiException("KOPIS API 상세 조회 실패: ${e.statusCode}", cause = e)
        } catch (e: Exception) {
            logger.error("KOPIS API 상세 조회 중 예외 발생", e)
            throw ExternalApiException("KOPIS API 상세 조회 중 예외 발생", cause = e)
        }
    }

    /**
     * 디버깅을 위한 API 설정 정보 확인
     */
    fun getDebugInfo(): Map<String, Any> {
        return mapOf(
            "baseUrl" to baseUrl,
            "apiKeyConfigured" to apiKey.isNotBlank(),
            "apiKeyLength" to apiKey.length,
            "apiKeyPrefix" to if (apiKey.length >= 10) apiKey.take(10) + "..." else apiKey
        )
    }
}