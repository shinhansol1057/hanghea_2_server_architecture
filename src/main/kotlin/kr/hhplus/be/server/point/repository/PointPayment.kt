package kr.hhplus.be.server.point.repository

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import kr.hhplus.be.server.user.repository.User
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "point_payments", schema = "hhplus")
class PointPayment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User? = null,

    @NotNull
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    val price: BigDecimal? = null,

    @NotNull
    @Column(name = "state", nullable = false)
    val state: Instant? = null,

    @Size(max = 255)
    @NotNull
    @Column(name = "method", nullable = false)
    val method: String? = null,

    @NotNull
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant? = null,

    @Column(name = "deleted_at")
    val deletedAt: Instant? = null,
) {

}
