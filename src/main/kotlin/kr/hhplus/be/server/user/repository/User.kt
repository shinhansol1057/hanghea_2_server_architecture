package kr.hhplus.be.server.user.repository

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant

@Entity
@Table(name = "users", schema = "hhplus")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    val email: String? = null,

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    val password: String? = null,

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    val name: String? = null,

    @Size(max = 255)
    @NotNull
    @Column(name = "phone_number", nullable = false)
    val phoneNumber: String? = null,

    @NotNull
    @Column(name = "point", nullable = false)
    val point: Long? = null,

    @Size(max = 255)
    @NotNull
    @Column(name = "state", nullable = false)
    val state: String? = null,

    @NotNull
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant? = null,

    @Column(name = "updated_at")
    val updatedAt: Instant? = null,

    @Column(name = "deleted_at")
    val deletedAt: Instant? = null
) {

}
