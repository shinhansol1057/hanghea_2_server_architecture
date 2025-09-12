package kr.hhplus.be.server.product.repository

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "products")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    val category: Category,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "price", nullable = false)
    val price: BigDecimal,

    @Column(name = "discount_price", nullable = false)
    val discountPrice: BigDecimal,

    @Column(name = "stock", nullable = false)
    val stock: Int,

    @Column(name = "cumulative_sale_count", nullable = false)
    val cumulativeSaleCount: Long,

    @Column(name = "description", nullable = false)
    val description: String = "",

    @Column(name = "state", nullable = false)
    val state: State = State.ACTIVE,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null,

    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime? = null,
) {

    enum class Category(val value: String) {
        UNIFORM("UNIFORM"), JERSEY("JERSEY"), T_SHIRT("T-SHIRT"),
        SHORTS("SHORTS"), HOODIE("HOODIE"), COMPRESSION("COMPRESSION"),
        SHOES("SHOES"), SOCKS("SOCKS");
    }

    enum class State(val value: String) {
        ACTIVE("ACTIVE"), DELETE("DELETE");
    }

}
