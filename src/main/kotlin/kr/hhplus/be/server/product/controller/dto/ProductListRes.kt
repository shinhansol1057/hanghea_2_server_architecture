package kr.hhplus.be.server.product.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "상품 목록 응답")
data class ProductListRes(
    @field:Schema(description = "상품 목록", example = """
        [
          {
            "id": 10,
            "name": "샘플 상품",
            "price": 10000,
            "stock": 50
          },
          {
            "id": 11,
            "name": "샘플 상품2",
            "price": 20000,
            "stock": 30
          }
        ]
    """)
    val products: List<ProductDetailRes>,
)
