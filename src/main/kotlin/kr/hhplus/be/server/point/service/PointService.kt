package kr.hhplus.be.server.point.service

import kr.hhplus.be.server.point.controller.dto.ChargeRes
import kr.hhplus.be.server.point.controller.dto.GetPointRes
import kr.hhplus.be.server.point.repository.PointPayment
import kr.hhplus.be.server.point.repository.PointPaymentJpa
import kr.hhplus.be.server.user.repository.User
import kr.hhplus.be.server.user.repository.UserJpa
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@Service
class PointService(
    private val userJpa: UserJpa,
    private val pointPaymentJpa: PointPaymentJpa
) {
    @Transactional
    fun chargePoint(userId: Long, amount: Int): ChargeRes {
        val user = userJpa.findById(userId)
            .getOrNull()
            ?: throw NotFoundException()

        val updatedUser = User(
            id = user.id,
            email = user.email,
            password = user.password,
            name = user.name,
            phoneNumber = user.phoneNumber,
            point = (user.point ?: 0) + amount,
            state = user.state,
            createdAt = user.createdAt,
            updatedAt = Instant.now(),
            deletedAt = user.deletedAt
        )
        userJpa.save(updatedUser)

        val pointPayment = PointPayment(
            user = user,
            price = BigDecimal.valueOf(amount.toLong()),
            state = Instant.now(),
            method = "CHARGE",
            createdAt = Instant.now()
        )
        pointPaymentJpa.save(pointPayment)

        return ChargeRes(userId, updatedUser.point?.toInt() ?: 0)
    }

    fun getPoint(userId: Long): GetPointRes {
        val user = userJpa.findById(userId)
            .getOrNull()
            ?: throw NotFoundException()

        return GetPointRes(userId, user.point?.toInt() ?: 0)
    }
}
