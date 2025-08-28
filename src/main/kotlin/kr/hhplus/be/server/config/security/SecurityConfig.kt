package kr.hhplus.be.server.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // REST/JWT 환경이면 보통 끔 (세션/폼로그인 쓰면 대신 CSRF 토큰 처리 필요)
            .cors { }              // 아래 CORS 설정 사용
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    // Swagger/헬스 체크 허용
                    .requestMatchers(
                        "/swagger-ui/**", "/v3/api-docs/**", "/actuator/health"
                    ).permitAll()

                    // 개발 중 공개할 엔드포인트 (필요 시 authenticated()로 바꾸세요)
                    .requestMatchers(HttpMethod.POST, "/point/charge").permitAll()

                    // 그 외는 인증 요구
                    .anyRequest().permitAll()
            }
            // 개발 중엔 Basic로 간단 인증(운영은 JWT/세션 중 택1)
            .httpBasic(Customizer.withDefaults())
            .formLogin { it.disable() }

        return http.build()
    }

    // (선택) 로컬 테스트용 인메모리 계정
    @Bean
    fun users(): UserDetailsService =
        InMemoryUserDetailsManager(
            User.withUsername("user").password("user").roles("USER").build()
        )

    // (선택) CORS – 프론트가 http://localhost:3000에서 호출한다면
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val c = CorsConfiguration()
        c.allowedOrigins = listOf("http://localhost:3000", "http://127.0.0.1:3000")
        c.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        c.allowedHeaders = listOf("*")
        c.allowCredentials = true
        val src = UrlBasedCorsConfigurationSource()
        src.registerCorsConfiguration("/**", c)
        return src
    }
}
