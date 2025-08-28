package kr.hhplus.be.server.order.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "상품 주문 요청")
data class OrderProductReq (
    @field:Schema(description = "사용자 ID", example = "1")
    val userId: Long,

    @field:Schema(description = "상품 ID", example = "1")
    val productId: Long,

    @field:Schema(description = "상품 수량", example = "2")
    val quantity: Int
)
