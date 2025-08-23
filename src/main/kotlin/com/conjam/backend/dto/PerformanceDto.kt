package com.conjam.backend.dto

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "공연 목록 조회 응답")
@JacksonXmlRootElement(localName = "dbs")
data class PerformanceListResponse(
    @Schema(description = "공연 목록")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "db")
    val performances: List<Performance> = emptyList()
)

@Schema(description = "공연 기본 정보")
data class Performance(
    @Schema(description = "공연 ID", example = "PF001234")
    @JacksonXmlProperty(localName = "mt20id")
    val id: String? = null,

    @Schema(description = "공연명", example = "라이온 킹")
    @JacksonXmlProperty(localName = "prfnm")
    val title: String? = null,

    @Schema(description = "공연 시작일", example = "2025-01-01")
    @JacksonXmlProperty(localName = "prfpdfrom")
    val startDate: String? = null,

    @Schema(description = "공연 종료일", example = "2025-12-31")
    @JacksonXmlProperty(localName = "prfpdto")
    val endDate: String? = null,

    @Schema(description = "공연시설명", example = "디큐브 링크 아트센터")
    @JacksonXmlProperty(localName = "fcltynm")
    val facilityName: String? = null,

    @Schema(description = "포스터 이미지 URL")
    @JacksonXmlProperty(localName = "poster")
    val poster: String? = null,

    @Schema(description = "지역", example = "서울특별시")
    @JacksonXmlProperty(localName = "area")
    val area: String? = null,

    @Schema(description = "장르", example = "뮤지컬")
    @JacksonXmlProperty(localName = "genrenm")
    val genre: String? = null,

    @Schema(description = "공연상태", example = "공연중")
    @JacksonXmlProperty(localName = "prfstate")
    val state: String? = null,

    @Schema(description = "오픈런 여부", example = "N")
    @JacksonXmlProperty(localName = "openrun")
    val openRun: String? = null
)

@Schema(description = "공연 상세 조회 응답")
@JacksonXmlRootElement(localName = "dbs")
data class PerformanceDetailResponse(
    @Schema(description = "공연 상세 정보")
    @JacksonXmlProperty(localName = "db")
    val performance: PerformanceDetail? = null
)

@Schema(description = "공연 상세 정보")
data class PerformanceDetail(
    @Schema(description = "공연 ID", example = "PF001234")
    @JacksonXmlProperty(localName = "mt20id")
    val id: String? = null,

    @Schema(description = "공연시설 ID", example = "FC000001")
    @JacksonXmlProperty(localName = "mt10id")
    val facilityId: String? = null,

    @Schema(description = "공연명", example = "라이온 킹")
    @JacksonXmlProperty(localName = "prfnm")
    val title: String? = null,

    @JacksonXmlProperty(localName = "prfpdfrom")
    val startDate: String? = null,

    @JacksonXmlProperty(localName = "prfpdto")
    val endDate: String? = null,

    @JacksonXmlProperty(localName = "fcltynm")
    val facilityName: String? = null,

    @Schema(description = "출연진 정보")
    @JacksonXmlProperty(localName = "prfcast")
    val cast: String? = null,

    @Schema(description = "제작진 정보")
    @JacksonXmlProperty(localName = "prfcrew")
    val crew: String? = null,

    @Schema(description = "공연 런타임", example = "150분")
    @JacksonXmlProperty(localName = "prfruntime")
    val runtime: String? = null,

    @JacksonXmlProperty(localName = "prfage")
    val ageLimit: String? = null,

    @JacksonXmlProperty(localName = "entrpsnmP")
    val producer: String? = null,

    @JacksonXmlProperty(localName = "entrpsnmA")
    val planner: String? = null,

    @JacksonXmlProperty(localName = "entrpsnmH")
    val host: String? = null,

    @JacksonXmlProperty(localName = "entrpsnmS")
    val organizer: String? = null,

    @Schema(description = "티켓 가격 정보")
    @JacksonXmlProperty(localName = "pcseguidance")
    val ticketPrice: String? = null,

    @Schema(description = "포스터 이미지 URL")
    @JacksonXmlProperty(localName = "poster")
    val poster: String? = null,

    @Schema(description = "공연 줄거리")
    @JacksonXmlProperty(localName = "sty")
    val synopsis: String? = null,

    @JacksonXmlProperty(localName = "genrenm")
    val genre: String? = null,

    @JacksonXmlProperty(localName = "prfstate")
    val state: String? = null,

    @JacksonXmlProperty(localName = "openrun")
    val openRun: String? = null,

    @JacksonXmlProperty(localName = "visit")
    val visit: String? = null,

    @JacksonXmlProperty(localName = "child")
    val child: String? = null,

    @JacksonXmlProperty(localName = "daehakro")
    val daehakro: String? = null,

    @JacksonXmlProperty(localName = "festival")
    val festival: String? = null,

    @JacksonXmlProperty(localName = "musicallicense")
    val musicalLicense: String? = null,

    @JacksonXmlProperty(localName = "musicalcreate")
    val musicalCreate: String? = null,

    @JacksonXmlProperty(localName = "updatedate")
    val updateDate: String? = null,

    @JacksonXmlProperty(localName = "dtguidance")
    val performanceTime: String? = null,

    @JacksonXmlElementWrapper(localName = "styurls")
    @JacksonXmlProperty(localName = "styurl")
    val introImages: List<String>? = null
)