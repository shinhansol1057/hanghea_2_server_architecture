package kr.hhplus.be.server.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJpa : JpaRepository<User, Long> {
}
