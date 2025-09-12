package kr.hhplus.be.server.order.service

import kr.hhplus.be.server.order.controller.dto.OrderProductReq
import kr.hhplus.be.server.order.controller.dto.OrderProductRes
import kr.hhplus.be.server.order.repository.Order
import kr.hhplus.be.server.order.repository.OrderJpa
import kr.hhplus.be.server.product.repository.ProductJpa
import kr.hhplus.be.server.user.repository.User
import kr.hhplus.be.server.user.repository.UserJpa
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

@Service
class OrderService(
    private val productJpa: ProductJpa,
    private val orderJpa: OrderJpa,
    private val userJpa: UserJpa
) {

    @Transactional
    fun orderProduct(req: OrderProductReq): OrderProductRes {
        val product = productJpa.findById(req.productId)
            .getOrNull()
            ?: throw NotFoundException()

        val user = userJpa.findById(req.userId)
            .getOrNull()
            ?: throw NotFoundException()

        if (product.stock < req.amount) {
            throw IllegalStateException("상품의 재고가 부족합니다.")
        }

        val totalPrice = product.discountPrice.multiply(req.amount.toBigDecimal())

        if (user.point == null || user.point < totalPrice.toLong()) {
            throw IllegalStateException("포인트가 부족합니다.")
        }

        val updatedUser = User(
            id = user.id,
            email = user.email,
            password = user.password,
            name = user.name,
            phoneNumber = user.phoneNumber,
            point = user.point - totalPrice.toLong(),
            state = user.state,
            createdAt = user.createdAt,
            updatedAt = Instant.now(),
            deletedAt = user.deletedAt
        )
        userJpa.save(updatedUser)

        val order = Order(
            user = user,
            product = product,
            amount = req.amount,
            pricePerUnit = product.price,
            discountPricePerUnit = product.discountPrice,
            deliveryMemo = req.deliveryMemo,
            deliveryAddress = req.deliveryAddress,
            state = Order.State.RECEIVED,
            createdAt = LocalDateTime.now()
        )

        val savedOrder = orderJpa.save(order)

        return OrderProductRes(
            id = savedOrder.id,
            userId = savedOrder.user.id ?: 0L,
            productId = product.id,
            productName = product.name,
            productCategory = product.category,
            amount = savedOrder.amount ?: 0,
            pricePerUnit = savedOrder.pricePerUnit ?: product.price,
            discountPricePerUnit = savedOrder.discountPricePerUnit ?: product.discountPrice,
            deliveryMemo = savedOrder.deliveryMemo ?: "",
            deliveryAddress = savedOrder.deliveryAddress ?: "",
            invoiceNumber = savedOrder.invoiceNumber,
            deliveryStartDate = savedOrder.deliveryStartDate,
            deliveryEndDate = savedOrder.deliveryEndDate,
            state = savedOrder.state,
            createdAt = savedOrder.createdAt,
            updatedAt = savedOrder.updatedAt,
            deletedAt = savedOrder.deletedAt
        )
    }
}
