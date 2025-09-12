package kr.hhplus.be.server.order.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.hhplus.be.server.order.repository.Order
import kr.hhplus.be.server.product.repository.Product
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime

@Schema(description = "상품 주문 응답")
data class OrderProductRes(
    @field:Schema(description = "주문 ID", example = "1")
    val id: Long,

    @field:Schema(description = "사용자 ID", example = "1")
    val userId: Long,

    @field:Schema(description = "상품 ID", example = "1")
    val productId: Long,

    @field:Schema(description = "상품명", example = "농구 유니폼")
    val productName: String,

    @field:Schema(description = "상품 카테고리", example = "UNIFORM")
    val productCategory: Product.Category,

    @field:Schema(description = "주문 수량", example = "2")
    val amount: Int,

    @field:Schema(description = "단위 가격", example = "10000.00")
    val pricePerUnit: BigDecimal,

    @field:Schema(description = "할인 단위 가격", example = "9000.00")
    val discountPricePerUnit: BigDecimal,

    @field:Schema(description = "배송 메모", example = "문 앞에 놓아주세요")
    val deliveryMemo: String,

    @field:Schema(description = "배송 주소", example = "서울시 강남구 테헤란로 123")
    val deliveryAddress: String,

    @field:Schema(description = "송장 번호", example = "123456789")
    val invoiceNumber: String?,

    @field:Schema(description = "배송 시작 일시", example = "2023-01-01T12:00:00Z")
    val deliveryStartDate: LocalDateTime?,

    @field:Schema(description = "배송 완료 일시", example = "2023-01-03T12:00:00Z")
    val deliveryEndDate: LocalDateTime?,

    @field:Schema(description = "주문 상태", example = "RECEIVED")
    val state: Order.State,

    @field:Schema(description = "주문 생성 시간", example = "2023-01-01T12:00:00")
    val createdAt: LocalDateTime,

    @field:Schema(description = "주문 수정 시간", example = "2023-01-01T12:30:00")
    val updatedAt: LocalDateTime?,

    @field:Schema(description = "주문 삭제 시간", example = "2023-01-10T12:00:00Z")
    val deletedAt: LocalDateTime?
)
