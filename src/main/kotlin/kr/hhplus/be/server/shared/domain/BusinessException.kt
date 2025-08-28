package kr.hhplus.be.server.shared.domain

open class BusinessException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause)

class UserNotFoundException(message: String? = null) : BusinessException(message)
class ConflictException(message: String? = null) : BusinessException(message)
class RateLimitException(message: String? = null) : BusinessException(message)
