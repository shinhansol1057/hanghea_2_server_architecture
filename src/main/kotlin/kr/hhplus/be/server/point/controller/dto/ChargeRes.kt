package kr.hhplus.be.server.point.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "포인트 충전 응답 DTO")
data class ChargeRes(
    @field:Schema(description = "유저 ID", example = "123")
    val userId: Long,

    @field:Schema(description = "충전 후 잔액", example = "50000")
    val balance: Int
)
