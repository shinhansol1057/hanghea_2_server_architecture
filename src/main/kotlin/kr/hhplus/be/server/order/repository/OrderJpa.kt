package kr.hhplus.be.server.order.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderJpa : JpaRepository<Order, Long> {
    fun findByUserId(userId: Long): List<Order>
}
