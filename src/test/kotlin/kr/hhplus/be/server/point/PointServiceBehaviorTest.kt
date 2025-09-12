package kr.hhplus.be.server.point

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.*
import kr.hhplus.be.server.point.controller.dto.ChargeRes
import kr.hhplus.be.server.point.controller.dto.GetPointRes
import kr.hhplus.be.server.point.repository.PointPayment
import kr.hhplus.be.server.point.repository.PointPaymentJpa
import kr.hhplus.be.server.point.service.PointService
import kr.hhplus.be.server.user.repository.User
import kr.hhplus.be.server.user.repository.UserJpa
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import java.math.BigDecimal
import java.time.Instant
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class PointServiceBehaviorTest : BehaviorSpec({

    val userJpa = mockk<UserJpa>()
    val pointPaymentJpa = mockk<PointPaymentJpa>()
    val pointService = PointService(userJpa, pointPaymentJpa)

    beforeTest {
        clearMocks(userJpa, pointPaymentJpa, recordedCalls = true, answers = false)
    }

    given("포인트 충전 로직") {

        `when`("유효한 사용자 ID와 충전 금액이 주어졌을 때") {
            lateinit var chargeRes: ChargeRes

            beforeTest {
                val user = User(
                    id = 1L,
                    email = "user@example.com",
                    password = "password",
                    name = "Test User",
                    phoneNumber = "010-1234-5678",
                    point = 10000L,
                    state = "ACTIVE",
                    createdAt = Instant.now()
                )

                val updatedUser = User(
                    id = 1L,
                    email = "user@example.com",
                    password = "password",
                    name = "Test User",
                    phoneNumber = "010-1234-5678",
                    point = 15000L, // 5000 포인트 충전 후
                    state = "ACTIVE",
                    createdAt = user.createdAt,
                    updatedAt = Instant.now()
                )

                every { userJpa.findById(1L) } returns Optional.of(user)
                every { userJpa.save(any()) } returns updatedUser

                val pointPayment = PointPayment(
                    id = 1L,
                    user = user,
                    price = BigDecimal.valueOf(5000),
                    state = Instant.now(),
                    method = "CHARGE",
                    createdAt = Instant.now()
                )

                every { pointPaymentJpa.save(any()) } returns pointPayment

                chargeRes = pointService.chargePoint(1L, 5000)
            }

            then("충전된 포인트가 반영된 잔액이 반환되어야 한다") {
                chargeRes.userId shouldBe 1L
                chargeRes.balance shouldBe 15000
            }

            then("UserJpa의 findById가 1회 호출되어야 한다") {
                verify(exactly = 1) { userJpa.findById(1L) }
            }

            then("UserJpa의 save가 1회 호출되어야 한다") {
                verify(exactly = 1) { userJpa.save(any()) }
            }

            then("PointPaymentJpa의 save가 1회 호출되어야 한다") {
                verify(exactly = 1) { pointPaymentJpa.save(any()) }
                confirmVerified(userJpa, pointPaymentJpa)
            }
        }

        `when`("존재하지 않는 사용자 ID로 충전을 시도할 때") {

            beforeTest {
                every { userJpa.findById(999L) } returns Optional.empty()
            }

            then("NotFoundException이 발생해야 한다") {
                val exception = shouldThrow<NotFoundException> {
                    pointService.chargePoint(999L, 5000)
                }

                exception::class shouldBe NotFoundException::class

                verify(exactly = 1) { userJpa.findById(999L) }
                confirmVerified(userJpa, pointPaymentJpa)
            }
        }
    }

    given("포인트 조회 로직") {

        `when`("유효한 사용자 ID가 주어졌을 때") {
            lateinit var getPointRes: GetPointRes

            beforeTest {
                val user = User(
                    id = 1L,
                    email = "user@example.com",
                    password = "password",
                    name = "Test User",
                    phoneNumber = "010-1234-5678",
                    point = 10000L,
                    state = "ACTIVE",
                    createdAt = Instant.now()
                )

                every { userJpa.findById(1L) } returns Optional.of(user)

                getPointRes = pointService.getPoint(1L)
            }

            then("사용자의 포인트 잔액이 반환되어야 한다") {
                getPointRes.userId shouldBe 1L
                getPointRes.balance shouldBe 10000
            }

            then("UserJpa의 findById가 1회 호출되어야 한다") {
                verify(exactly = 1) { userJpa.findById(1L) }
                confirmVerified(userJpa, pointPaymentJpa)
            }
        }

        `when`("존재하지 않는 사용자 ID로 조회를 시도할 때") {

            beforeTest {
                every { userJpa.findById(999L) } returns Optional.empty()
            }

            then("NotFoundException이 발생해야 한다") {
                val exception = shouldThrow<NotFoundException> {
                    pointService.getPoint(999L)
                }

                exception::class shouldBe NotFoundException::class

                verify(exactly = 1) { userJpa.findById(999L) }
                confirmVerified(userJpa, pointPaymentJpa)
            }
        }
    }
})
