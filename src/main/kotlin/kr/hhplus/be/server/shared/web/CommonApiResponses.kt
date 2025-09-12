package kr.hhplus.be.server.shared.web

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ProblemDetail


@ApiResponses(
    value = [
        ApiResponse(
            responseCode = "400",
            description = "검증 실패/잘못된 요청",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ProblemDetail::class),
                examples = [ExampleObject(
                    name = "validation-error",
                    value = """{"type":"about:blank","title":"Invalid_Request","status":400,"detail":"요청 파라미터가 유효하지 않습니다","code":"INVALID_REQUEST"}"""
                )]
            )]
        ),
        ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ProblemDetail::class),
                examples = [ExampleObject(
                    name = "unauthorized",
                    value = """{"type":"about:blank","title":"Unauthorized","status":401,"detail":"인증이 필요합니다.","code":"AUTH_UNAUTHORIZED"}"""
                )]
            )]
        ),
        ApiResponse(
            responseCode = "409",
            description = "충돌(중복/동시성 등)",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ProblemDetail::class),
                examples = [ExampleObject(
                    name = "conflict",
                    value = """{"type":"about:blank","title":"Conflict","status":409,"detail":"이미 처리된 요청입니다.","code":"CONFLICT"}"""
                )]
            )]
        ),
        ApiResponse(
            responseCode = "500",
            description = "서버 오류",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ProblemDetail::class),
                examples = [ExampleObject(
                    name = "internal-error",
                    value = """{"type":"about:blank","title":"Internal Server Error","status":500,"detail":"일시적인 오류가 발생했습니다.","code":"INTERNAL_ERROR"}"""
                )]
            )]
        )
    ]
)
annotation class CommonErrorResponses

