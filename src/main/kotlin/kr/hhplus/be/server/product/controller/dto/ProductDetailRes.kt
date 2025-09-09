package kr.hhplus.be.server.product.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "상품 정보 조회 응답 DTO")
data class ProductDetailRes(
    @field:Schema(description = "상품 ID", example = "10")
    val productId: Long,

    @field:Schema(description = "상품명", example = "샘플 상품")
    val name: String,

    @field:Schema(description = "상품 가격", example = "10000")
    val price: Int,

    @field:Schema(description = "상품 재고", example = "50")
    val stock: Int
)
