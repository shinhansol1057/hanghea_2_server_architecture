package kr.hhplus.be.server.point.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PointPaymentJpa : JpaRepository<PointPayment, Long> {
    fun findByUserId(userId: Long): List<PointPayment>
}
