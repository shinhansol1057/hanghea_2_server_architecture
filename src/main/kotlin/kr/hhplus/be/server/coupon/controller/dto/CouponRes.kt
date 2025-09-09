package kr.hhplus.be.server.coupon.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "쿠폰 응답 DTO")
data class CouponRes (
    @field:Schema(description = "쿠폰 ID", example = "1")
    val couponId: Long,

    @field:Schema(description = "쿠폰 이름", example = "5000원 쿠폰")
    val name: String,

    @field:Schema(description = "쿠폰 가격", example = "5000")
    val price: Int,

    @field:Schema(description = "만료 시간", example = "2025년 12월 31일")
    val expirationDate: String,

    @field:Schema(description = "쿠폰 사용 여부", example = "false")
    val isUsed: Boolean,

    @field:Schema(description = "쿠폰 생성 시간", example = "2025년 01월 01일 12시 00분 00초")
    val createdAt: String
)
