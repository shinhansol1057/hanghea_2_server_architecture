package kr.hhplus.be.server.order.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "상품 주문 요청")
data class OrderProductReq (
    @field:Schema(description = "사용자 ID", example = "1")
    val userId: Long,

    @field:Schema(description = "상품 ID", example = "1")
    val productId: Long,

    @field:Schema(description = "상품 수량", example = "2")
    val amount: Int,

    @field:Schema(description = "배송 메모", example = "문 앞에 놓아주세요")
    val deliveryMemo: String,

    @field:Schema(description = "배송 주소", example = "서울시 강남구 테헤란로 123")
    val deliveryAddress: String
)
