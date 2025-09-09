package kr.hhplus.be.server.product.service

import kr.hhplus.be.server.product.controller.dto.ProductDetailRes
import kr.hhplus.be.server.product.controller.dto.ProductListRes
import org.springframework.stereotype.Service

@Service
class ProductService {

    fun getProductList(): ProductListRes {
        return ProductListRes(listOf())
    }

    fun getTopBestSellers(): ProductListRes {
        return ProductListRes(listOf())
    }

    fun getProductDetail(productId: Long): ProductDetailRes {
        return ProductDetailRes(10, "샘플 상품", 10000, 50)
    }

}
