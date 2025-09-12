package kr.hhplus.be.server.order.repository

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import kr.hhplus.be.server.product.repository.Product
import kr.hhplus.be.server.user.repository.User
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,

    @NotNull
    @Column(name = "amount", nullable = false)
    val amount: Int? = null,

    @NotNull
    @Column(name = "price_per_unit", nullable = false, precision = 10, scale = 2)
    val pricePerUnit: BigDecimal? = null,

    @Column(name = "discount_price_per_unit", precision = 10, scale = 2)
    val discountPricePerUnit: BigDecimal? = null,

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    val state: State = State.RECEIVED,

    @Size(max = 255)
    @Column(name = "invoice_number")
    val invoiceNumber: String? = null,

    @Column(name = "delivery_start_date")
    val deliveryStartDate: LocalDateTime? = null,

    @Column(name = "delivery_end_date")
    val deliveryEndDate: LocalDateTime? = null,

    @Size(max = 255)
    @NotNull
    @Column(name = "delivery_memo", nullable = false)
    val deliveryMemo: String? = null,

    @Size(max = 255)
    @NotNull
    @Column(name = "delivery_address", nullable = false)
    val deliveryAddress: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null,

    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime? = null
) {

    enum class State(val value: String) {
        RECEIVED("RECEIVED"), DELIVERING("DELIVERING"), COMPLETED("COMPLETED"), CANCELED("CANCELED");
    }
}
