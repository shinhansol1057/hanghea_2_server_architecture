package kr.hhplus.be.server.product.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kr.hhplus.be.server.product.controller.dto.ProductDetailRes
import kr.hhplus.be.server.product.controller.dto.ProductListRes
import kr.hhplus.be.server.product.service.ProductService
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
@RequestMapping("/product")
class ProductController(
    private val productService: ProductService
) {
    @GetMapping("/list")
    @Operation(
        summary = "상품 리스트 조회",
        description = "상품 리스트를 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "상품 리스트 조회 성공",
        content = [Content(schema = Schema(implementation = ProductListRes::class))]
    )
    fun getProductList(): ProductListRes {
        return productService.getProductList()
    }

    @GetMapping("/best-sellers")
    @Operation(
        summary = "최근 베스트셀러 Top 3 조회",
        description = "최근 판매량이 많은 상위 3개 상품을 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "베스트셀러 조회 성공",
        content = [Content(schema = Schema(implementation = ProductListRes::class))]
    )
    fun getTop3BestSellers(): ProductListRes {
        return productService.getTopBestSellers()
    }

    @GetMapping("/{productId}")
    @Operation(
        summary = "상품 정보 조회",
        description = "상품 ID로 상품의 상세 정보를 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "상품 정보 조회 성공",
        content = [Content(schema = Schema(implementation = ProductDetailRes::class))]
    )
    @ApiResponse(
        responseCode = "404",
        description = "상품을 찾을 수 없음",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ProblemDetail::class),
            examples = [ExampleObject(
                name = "not-found",
                value = """{"type":"about:blank","title":"Product Not Found","status":404,"detail":"상품 정보를 찾을 수 없습니다.","code":"NOT_FOUND"}"""
            )]
        )]
    )
    fun getProductDetail(
        @Parameter(
            description = "조회할 상품의 ID",
            example = "101"
        )
        @PathVariable("productId") productId: Long,
    ): ProductDetailRes {
        return productService.getProductDetail(productId)
    }
}
