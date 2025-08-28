package kr.hhplus.be.server.shared.web

import kr.hhplus.be.server.shared.domain.*
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingServletRequestParameterException
import jakarta.validation.ConstraintViolationException
import org.springframework.web.client.HttpClientErrorException.UnprocessableEntity

@RestControllerAdvice
@Order(1)
class GlobalExceptionHandler {

    private fun pd(
        status: HttpStatus,
        title: String,
        detail: String? = null,
        code: String? = null,
        extra: Map<String, Any?> = emptyMap()
    ): ProblemDetail {
        val p = ProblemDetail.forStatus(status)
        p.title = title
        p.detail = detail ?: title
        if (code != null) p.setProperty("code", code)
        extra.forEach { (k, v) -> if (v != null) p.setProperty(k, v) }
        return p
    }

    // 400: JSON 파싱 오류
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleUnreadable(ex: HttpMessageNotReadableException) =
        pd(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "본문(JSON)이 올바르지 않습니다.", "INVALID_JSON")

    // 400: @Valid 바디 검증 오류
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ProblemDetail {
        val errors = ex.bindingResult.fieldErrors.map {
            mapOf("field" to it.field, "reason" to (it.defaultMessage ?: "invalid"))
        }
        return pd(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "요청 파라미터가 유효하지 않습니다.", "INVALID_REQUEST",
            mapOf("errors" to errors))
    }

    // 400: @Validated(파라미터/경로 변수) 검증 오류
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraint(ex: ConstraintViolationException) =
        pd(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", ex.message, "INVALID_REQUEST")

    // 400: 필수 쿼리/폼 파라미터 누락
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParam(ex: MissingServletRequestParameterException) =
        pd(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "${ex.parameterName} 파라미터가 필요합니다.", "MISSING_PARAM")

    // 401: Spring Security 인증 실패
    @ExceptionHandler(AuthenticationException::class)
    fun handleAuth(ex: AuthenticationException) =
        pd(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.message ?: "인증이 필요합니다.", "AUTH_UNAUTHORIZED")

    // 403: 권한 부족
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(ex: AccessDeniedException) =
        pd(HttpStatus.FORBIDDEN, "Forbidden", ex.message ?: "권한이 없습니다.", "AUTH_FORBIDDEN")

    // 404: 내가 정의한 도메인 404
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(ex: UserNotFoundException) =
        pd(HttpStatus.NOT_FOUND, "User Not Found", ex.message ?: "유저를 찾을 수 없습니다.", "USER_NOT_FOUND")

    // 409: 비즈니스 충돌 or DB 유니크 제약 충돌
    @ExceptionHandler(ConflictException::class)
    fun handleConflict(ex: ConflictException) =
        pd(HttpStatus.CONFLICT, "Conflict", ex.message ?: "요청 충돌이 발생했습니다.", "CONFLICT")

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrity(ex: DataIntegrityViolationException) =
        pd(HttpStatus.CONFLICT, "Conflict", "데이터 무결성 제약을 위반했습니다.", "DATA_INTEGRITY")

    // 429: 레이트 리밋
    @ExceptionHandler(RateLimitException::class)
    fun handleRateLimit(ex: RateLimitException) =
        pd(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests", ex.message ?: "잠시 후 다시 시도해주세요.", "RATE_LIMIT")

    // 마지막 안전망
    @ExceptionHandler(Exception::class)
    fun handleEtc(ex: Exception) =
        pd(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "일시적인 오류가 발생했습니다.", "INTERNAL_ERROR",
            mapOf("traceId" to java.util.UUID.randomUUID().toString()))
}
