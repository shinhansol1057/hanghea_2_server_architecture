package kr.hhplus.be.server.coupon.repository

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import kr.hhplus.be.server.user.repository.User
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "coupons", schema = "hhplus")
class Coupon(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User? = null,

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    val name: String? = null,

    @NotNull
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    val price: BigDecimal? = null,

    @NotNull
    @Column(name = "is_used", nullable = false)
    val isUsed: Boolean? = false,

    @Column(name = "expiration_at")
    val expirationAt: Instant? = null,

    @NotNull
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant? = null,
) {

}
