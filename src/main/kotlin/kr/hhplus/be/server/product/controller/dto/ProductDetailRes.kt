package kr.hhplus.be.server.product.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.hhplus.be.server.product.repository.Product
import java.math.BigDecimal
import java.time.LocalDateTime

@Schema(description = "상품 정보 조회 응답 DTO")
data class ProductDetailRes(
    @field:Schema(description = "상품 ID", example = "10")
    val id: Long,

    @field:Schema(description = "카테고리", example = "UNIFORM")
    val category: Product.Category,

    @field:Schema(description = "상품명", example = "샘플 상품")
    val name: String,

    @field:Schema(description = "상품 가격", example = "10000")
    val price: BigDecimal,

    @field:Schema(description = "할인 가격", example = "3000")
    val discountPrice: BigDecimal,

    @field:Schema(description = "상품 재고", example = "50")
    val stock: Int,

    @field:Schema(description = "누적 판매 개수", example = "1000")
    val cumulativeSaleCount: Long,

    @field:Schema(description = "설명", example = "2025 베스트셀러 유니폼")
    val description: String,

    @field:Schema(description = "상태", example = "ACTIVE")
    val state: Product.State,

    @field:Schema(description = "생성일", example = "2025-10-10T00:00:00")
    val createdAt: LocalDateTime,
)
