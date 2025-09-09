package kr.hhplus.be.server.coupon.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kr.hhplus.be.server.coupon.controller.dto.CouponListRes
import kr.hhplus.be.server.coupon.controller.dto.CouponRes
import kr.hhplus.be.server.coupon.service.CouponService
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.*

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
@RequestMapping("/coupon")
class CouponController(
    private val couponService: CouponService
) {
    @GetMapping("/list/{userId}")
    @Operation(
        summary = "쿠폰 리스트 조회",
        description = "사용자의 쿠폰 리스트를 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "쿠폰 리스트 조회 성공",
        content = [Content(schema = Schema(implementation = CouponListRes::class))]
    )
    @ApiResponse(
        responseCode = "404",
        description = "유저를 찾을 수 없음",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ProblemDetail::class),
            examples = [ExampleObject(
                name = "not-found",
                value = """{"type":"about:blank","title":"User Not Found","status":404,"detail":"유저를 찾을 수 없습니다.","code":"USER_NOT_FOUND"}"""
            )]
        )]
    )
    fun getCouponList(
        @PathVariable("userId") userId: Long,
    ): CouponListRes {
        return couponService.getCouponList(userId)
    }


    @PostMapping("/limit/{userId}")
    @Operation(
        summary = "선착순 쿠폰 발급",
        description = "선착순 쿠폰을 발급합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "선착순 쿠폰 발급 완료",
        content = [Content(schema = Schema(implementation = CouponRes::class))]
    )
    @ApiResponse(
        responseCode = "404",
        description = "유저를 찾을 수 없음",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ProblemDetail::class),
            examples = [ExampleObject(
                name = "not-found",
                value = """{"type":"about:blank","title":"User Not Found","status":404,"detail":"유저를 찾을 수 없습니다.","code":"USER_NOT_FOUND"}"""
            )]
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "잔여 쿠폰이 없음",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ProblemDetail::class),
            examples = [ExampleObject(
                name = "not-found",
                value = """{"type":"about:blank","title":"Not Found","status":404,"detail":"잔여 쿠폰이 없습니다.","code":"NOT_FOUND"}"""
            )]
        )]
    )
    fun postLimitCoupon(
        @PathVariable("userId") userId: Long,
    ): CouponRes {
        return couponService.postLimitCoupon(userId)
    }
}
