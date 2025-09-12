package kr.hhplus.be.server.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductJpa : JpaRepository<Product, Long> {
}
