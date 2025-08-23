package com.conjam.backend.dto

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

// 공연 목록 조회 응답
@JacksonXmlRootElement(localName = "dbs")
data class PerformanceListResponse(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "db")
    val performances: List<Performance> = emptyList()
)

// 공연 기본 정보
data class Performance(
    @JacksonXmlProperty(localName = "mt20id")
    val id: String? = null,

    @JacksonXmlProperty(localName = "prfnm")
    val title: String? = null,

    @JacksonXmlProperty(localName = "prfpdfrom")
    val startDate: String? = null,

    @JacksonXmlProperty(localName = "prfpdto")
    val endDate: String? = null,

    @JacksonXmlProperty(localName = "fcltynm")
    val facilityName: String? = null,

    @JacksonXmlProperty(localName = "poster")
    val poster: String? = null,

    @JacksonXmlProperty(localName = "area")
    val area: String? = null,

    @JacksonXmlProperty(localName = "genrenm")
    val genre: String? = null,

    @JacksonXmlProperty(localName = "prfstate")
    val state: String? = null,

    @JacksonXmlProperty(localName = "openrun")
    val openRun: String? = null
)

// 공연 상세 조회 응답
@JacksonXmlRootElement(localName = "dbs")
data class PerformanceDetailResponse(
    @JacksonXmlProperty(localName = "db")
    val performance: PerformanceDetail? = null
)

// 공연 상세 정보
data class PerformanceDetail(
    @JacksonXmlProperty(localName = "mt20id")
    val id: String? = null,

    @JacksonXmlProperty(localName = "mt10id")
    val facilityId: String? = null,

    @JacksonXmlProperty(localName = "prfnm")
    val title: String? = null,

    @JacksonXmlProperty(localName = "prfpdfrom")
    val startDate: String? = null,

    @JacksonXmlProperty(localName = "prfpdto")
    val endDate: String? = null,

    @JacksonXmlProperty(localName = "fcltynm")
    val facilityName: String? = null,

    @JacksonXmlProperty(localName = "prfcast")
    val cast: String? = null,

    @JacksonXmlProperty(localName = "prfcrew")
    val crew: String? = null,

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

    @JacksonXmlProperty(localName = "pcseguidance")
    val ticketPrice: String? = null,

    @JacksonXmlProperty(localName = "poster")
    val poster: String? = null,

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