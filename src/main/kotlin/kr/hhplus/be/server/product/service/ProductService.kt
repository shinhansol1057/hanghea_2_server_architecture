package kr.hhplus.be.server.product.service

import kr.hhplus.be.server.product.controller.dto.ProductDetailRes
import kr.hhplus.be.server.product.controller.dto.ProductListRes
import kr.hhplus.be.server.product.repository.ProductJpa
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ProductService(
    private val productJpa: ProductJpa
) {

    fun getProductList(): ProductListRes {
        return ProductListRes(listOf())
    }

    fun getTopBestSellers(): ProductListRes {
        return ProductListRes(listOf())
    }

    fun getProductDetail(productId: Long): ProductDetailRes {
        val p = productJpa.findById(productId)
            .getOrNull()
            ?: throw NotFoundException()

        return ProductDetailRes(
            p.id,
            p.category,
            p.name,
            p.price,
            p.discountPrice,
            p.stock,
            p.cumulativeSaleCount,
            p.description,
            p.state,
            p.createdAt
        )
    }
}
