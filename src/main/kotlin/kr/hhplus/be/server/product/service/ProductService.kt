package kr.hhplus.be.server.product.service

import kr.hhplus.be.server.product.controller.dto.ProductDetailRes
import org.springframework.stereotype.Service

@Service
class ProductService {
    fun getProductDetail(productId: Long): ProductDetailRes {
        return ProductDetailRes(10, "샘플 상품", 10000, 50)
    }
}
