package kr.hhplus.be.server.point.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "포인트 충전 요청 DTO")
data class ChargeReq(
    @field:Schema(description = "유저 ID", example = "1004")
    val userId: Long,

    @field:Schema(description = "충전 금액", example = "5000")
    val amount: Int
)

