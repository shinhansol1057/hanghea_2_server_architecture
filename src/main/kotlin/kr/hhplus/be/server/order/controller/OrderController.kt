package kr.hhplus.be.server.order.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kr.hhplus.be.server.order.controller.dto.OrderProductReq
import kr.hhplus.be.server.order.service.OrderService
import kr.hhplus.be.server.point.controller.dto.ChargeReq
import kr.hhplus.be.server.point.controller.dto.ChargeRes
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@ApiResponses(
    value = [
        ApiResponse(
            responseCode = "400",
            description = "검증 실패/잘못된 요청",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ProblemDetail::class),
                examples = [ExampleObject(
                    name = "validation-error",
                    value = """{"type":"about:blank","title":"Invalid_Request","status":400,"detail":"요청 파라미터가 유효하지 않습니다","code":"INVALID_REQUEST"}"""
                )]
            )]
        ),
        ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ProblemDetail::class),
                examples = [ExampleObject(
                    name = "unauthorized",
                    value = """{"type":"about:blank","title":"Unauthorized","status":401,"detail":"인증이 필요합니다.","code":"AUTH_UNAUTHORIZED"}"""
                )]
            )]
        ),
        ApiResponse(
            responseCode = "409",
            description = "충돌(중복/동시성 등)",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ProblemDetail::class),
                examples = [ExampleObject(
                    name = "conflict",
                    value = """{"type":"about:blank","title":"Conflict","status":409,"detail":"이미 처리된 요청입니다.","code":"CONFLICT"}"""
                )]
            )]
        ),
        ApiResponse(
            responseCode = "500",
            description = "서버 오류",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ProblemDetail::class),
                examples = [ExampleObject(
                    name = "internal-error",
                    value = """{"type":"about:blank","title":"Internal Server Error","status":500,"detail":"일시적인 오류가 발생했습니다.","code":"INTERNAL_ERROR"}"""
                )]
            )]
        )
    ]
)
@RestController
@RequestMapping("/order")
class OrderController(
    private val orderService: OrderService
) {
    @PostMapping("/{productId}")
    @Operation(
        summary = "상품 주문",
        description = "상품을 주문하고 결제합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "주문 및 결제 성공",
    )
    @ApiResponse(
        responseCode = "422",
        description = "상품 재고 부족",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ProblemDetail::class),
            examples = [ExampleObject(
                name = "UnprocessableEntity",
                value = """{"type":"about:blank","title":"Unprocessable_Entity","status":422,"detail":"상품의 재고가 부족합니다.","code":"UNPROCESSABLE_ENTITY"}"""
            )]
        )]
    )
    @ApiResponse(
        responseCode = "422",
        description = "포인트 부족",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ProblemDetail::class),
            examples = [ExampleObject(
                name = "UnprocessableEntity",
                value = """{"type":"about:blank","title":"Unprocessable_Entity","status":422,"detail":"포인트가 부족합니다.","code":"UNPROCESSABLE_ENTITY"}"""
            )]
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "상품을 찾을 수 없음",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ProblemDetail::class),
            examples = [ExampleObject(
                name = "not-found",
                value = """{"type":"about:blank","title":"Not Found","status":404,"detail":"상품을 찾을 수 없습니다.","code":"NOT_FOUND"}"""
            )]
        )]
    )
    fun orderProduct(
        @RequestBody req: OrderProductReq,
    ) {
        orderService.orderProduct(req)
    }
}
