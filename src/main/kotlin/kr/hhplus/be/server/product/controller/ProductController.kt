package kr.hhplus.be.server.product.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import kr.hhplus.be.server.product.controller.dto.ProductDetailRes
import kr.hhplus.be.server.product.controller.dto.ProductListRes
import kr.hhplus.be.server.product.service.ProductService
import kr.hhplus.be.server.shared.web.CommonErrorResponses
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product")
class ProductController(
    private val productService: ProductService
) {

    @Operation(
        summary = "상품 리스트 조회",
        description = "상품 리스트를 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "상품 리스트 조회 성공",
        content = [Content(schema = Schema(implementation = ProductListRes::class))]
    )
    @CommonErrorResponses
    @GetMapping("/list")
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
