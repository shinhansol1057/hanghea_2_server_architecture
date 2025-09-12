package kr.hhplus.be.server.order

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.*
import kr.hhplus.be.server.order.controller.dto.OrderProductReq
import kr.hhplus.be.server.order.controller.dto.OrderProductRes
import kr.hhplus.be.server.order.repository.Order
import kr.hhplus.be.server.order.repository.OrderJpa
import kr.hhplus.be.server.order.service.OrderService
import kr.hhplus.be.server.product.repository.Product
import kr.hhplus.be.server.product.repository.ProductJpa
import kr.hhplus.be.server.user.repository.User
import kr.hhplus.be.server.user.repository.UserJpa
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull
import java.util.Optional

class OrderServiceBehaviorTest : BehaviorSpec({

    val productJpa = mockk<ProductJpa>()
    val orderJpa = mockk<OrderJpa>()
    val userJpa = mockk<UserJpa>()
    val orderService = OrderService(productJpa, orderJpa, userJpa)

    beforeTest {
        clearMocks(productJpa, orderJpa, userJpa, recordedCalls = true, answers = false)
    }

    given("상품 주문 로직") {

        `when`("유효한 상품 ID와 충분한 재고, 충분한 포인트가 있을 때") {
            lateinit var orderProductRes: OrderProductRes

            beforeTest {
                val product = Product(
                    id = 1L,
                    category = Product.Category.UNIFORM,
                    name = "농구 유니폼",
                    price = BigDecimal("10000.00"),
                    discountPrice = BigDecimal("9000.00"),
                    stock = 50,
                    cumulativeSaleCount = 200,
                    description = "신상 농구 유니폼",
                    state = Product.State.ACTIVE,
                    createdAt = LocalDateTime.parse("2025-10-01T10:15:30")
                )

                every { productJpa.findById(1L) } returns Optional.of(product)

                val user = User(
                    id = 1L,
                    email = "user@example.com",
                    password = "password",
                    name = "Test User",
                    phoneNumber = "010-1234-5678",
                    point = 50000L,
                    state = "ACTIVE",
                    createdAt = Instant.now()
                )

                every { userJpa.findById(1L) } returns Optional.of(user)
                every { userJpa.save(any()) } returns user

                val order = Order(
                    id = 1L,
                    user = user,
                    product = product,
                    amount = 2,
                    pricePerUnit = BigDecimal("10000.00"),
                    discountPricePerUnit = BigDecimal("9000.00"),
                    deliveryMemo = "문 앞에 놓아주세요",
                    deliveryAddress = "서울시 강남구 테헤란로 123",
                    state = Order.State.RECEIVED,
                    createdAt = LocalDateTime.parse("2025-10-01T10:30:00")
                )

                every { orderJpa.save(any()) } returns order

                val req = OrderProductReq(
                    userId = 1L,
                    productId = 1L,
                    amount = 2,
                    deliveryMemo = "문 앞에 놓아주세요",
                    deliveryAddress = "서울시 강남구 테헤란로 123"
                )

                orderProductRes = orderService.orderProduct(req)
            }

            then("주문이 성공적으로 생성되어야 한다") {
                orderProductRes.id shouldBe 1L
                orderProductRes.userId shouldBe 1L
                orderProductRes.productId shouldBe 1L
                orderProductRes.productName shouldBe "농구 유니폼"
                orderProductRes.productCategory shouldBe Product.Category.UNIFORM
                orderProductRes.amount shouldBe 2
                orderProductRes.pricePerUnit shouldBe BigDecimal("10000.00")
                orderProductRes.discountPricePerUnit shouldBe BigDecimal("9000.00")
                orderProductRes.deliveryMemo shouldBe "문 앞에 놓아주세요"
                orderProductRes.deliveryAddress shouldBe "서울시 강남구 테헤란로 123"
                orderProductRes.state shouldBe Order.State.RECEIVED
                orderProductRes.createdAt shouldBe LocalDateTime.parse("2025-10-01T10:30:00")
            }

            then("ProductJpa의 findById가 1회 호출되어야 한다") {
                verify(exactly = 1) { productJpa.findById(1L) }
            }

            then("UserJpa의 findById가 1회 호출되어야 한다") {
                verify(exactly = 1) { userJpa.findById(1L) }
            }

            then("UserJpa의 save가 1회 호출되어야 한다") {
                verify(exactly = 1) { userJpa.save(any()) }
            }

            then("OrderJpa의 save가 1회 호출되어야 한다") {
                verify(exactly = 1) { orderJpa.save(any()) }
                confirmVerified(productJpa, orderJpa, userJpa)
            }
        }

        `when`("존재하지 않는 상품 ID로 주문할 때") {

            beforeTest {
                every { productJpa.findById(999L) } returns Optional.empty()
            }

            then("NotFoundException이 발생해야 한다") {
                val req = OrderProductReq(
                    userId = 1L,
                    productId = 999L,
                    amount = 2,
                    deliveryMemo = "문 앞에 놓아주세요",
                    deliveryAddress = "서울시 강남구 테헤란로 123"
                )

                val exception = shouldThrow<NotFoundException> {
                    orderService.orderProduct(req)
                }

                exception::class shouldBe NotFoundException::class

                verify(exactly = 1) { productJpa.findById(999L) }
                confirmVerified(productJpa, orderJpa, userJpa)
            }
        }

        `when`("존재하지 않는 사용자 ID로 주문할 때") {

            beforeTest {
                val product = Product(
                    id = 1L,
                    category = Product.Category.UNIFORM,
                    name = "농구 유니폼",
                    price = BigDecimal("10000.00"),
                    discountPrice = BigDecimal("9000.00"),
                    stock = 50,
                    cumulativeSaleCount = 200,
                    description = "신상 농구 유니폼",
                    state = Product.State.ACTIVE,
                    createdAt = LocalDateTime.parse("2025-10-01T10:15:30")
                )

                every { productJpa.findById(1L) } returns Optional.of(product)
                every { userJpa.findById(999L) } returns Optional.empty()
            }

            then("NotFoundException이 발생해야 한다") {
                val req = OrderProductReq(
                    userId = 999L,
                    productId = 1L,
                    amount = 2,
                    deliveryMemo = "문 앞에 놓아주세요",
                    deliveryAddress = "서울시 강남구 테헤란로 123"
                )

                val exception = shouldThrow<NotFoundException> {
                    orderService.orderProduct(req)
                }

                exception::class shouldBe NotFoundException::class

                verify(exactly = 1) { productJpa.findById(1L) }
                verify(exactly = 1) { userJpa.findById(999L) }
                confirmVerified(productJpa, userJpa, orderJpa)
            }
        }

        `when`("재고가 부족할 때") {

            beforeTest {
                val product = Product(
                    id = 2L,
                    category = Product.Category.UNIFORM,
                    name = "농구 유니폼",
                    price = BigDecimal("10000.00"),
                    discountPrice = BigDecimal("9000.00"),
                    stock = 1, // 재고 1개
                    cumulativeSaleCount = 200,
                    description = "신상 농구 유니폼",
                    state = Product.State.ACTIVE,
                    createdAt = LocalDateTime.parse("2025-10-01T10:15:30")
                )

                every { productJpa.findById(2L) } returns Optional.of(product)
            }

            then("IllegalStateException이 발생해야 한다") {
                val req = OrderProductReq(
                    userId = 1L,
                    productId = 2L,
                    amount = 2, // 2개 주문 시도
                    deliveryMemo = "문 앞에 놓아주세요",
                    deliveryAddress = "서울시 강남구 테헤란로 123"
                )

                val exception = shouldThrow<IllegalStateException> {
                    orderService.orderProduct(req)
                }

                exception.message shouldBe "상품의 재고가 부족합니다."

                verify(exactly = 1) { productJpa.findById(2L) }
                confirmVerified(productJpa, orderJpa, userJpa)
            }
        }

        `when`("포인트가 부족할 때") {

            beforeTest {
                val product = Product(
                    id = 3L,
                    category = Product.Category.UNIFORM,
                    name = "농구 유니폼",
                    price = BigDecimal("10000.00"),
                    discountPrice = BigDecimal("9000.00"),
                    stock = 50,
                    cumulativeSaleCount = 200,
                    description = "신상 농구 유니폼",
                    state = Product.State.ACTIVE,
                    createdAt = LocalDateTime.parse("2025-10-01T10:15:30")
                )

                every { productJpa.findById(3L) } returns Optional.of(product)

                val user = User(
                    id = 2L,
                    email = "user2@example.com",
                    password = "password",
                    name = "Test User 2",
                    phoneNumber = "010-1234-5678",
                    point = 5000L, // 포인트 5000
                    state = "ACTIVE",
                    createdAt = Instant.now()
                )

                every { userJpa.findById(2L) } returns Optional.of(user)
            }

            then("IllegalStateException이 발생해야 한다") {
                val req = OrderProductReq(
                    userId = 2L,
                    productId = 3L,
                    amount = 10, // 10개 주문 시도 (9000 * 10 = 90000)
                    deliveryMemo = "문 앞에 놓아주세요",
                    deliveryAddress = "서울시 강남구 테헤란로 123"
                )

                val exception = shouldThrow<IllegalStateException> {
                    orderService.orderProduct(req)
                }

                exception.message shouldBe "포인트가 부족합니다."

                verify(exactly = 1) { productJpa.findById(3L) }
                verify(exactly = 1) { userJpa.findById(2L) }
                confirmVerified(productJpa, userJpa, orderJpa)
            }
        }
    }
})
