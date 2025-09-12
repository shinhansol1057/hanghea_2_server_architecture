package kr.hhplus.be.server.product.repository

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import kr.hhplus.be.server.user.repository.User
import java.time.Instant

@Entity
@Table(name = "carts", schema = "hhplus")
open class Cart(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User? = null,

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product? = null,

    @NotNull
    @Column(name = "amount", nullable = false)
    val amount: Int? = null,

    @NotNull
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant? = null,

    @Column(name = "updated_at")
    val updatedAt: Instant? = null
) {

}
