package kr.hhplus.be.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["kr.hhplus.be.server"])
@EnableJpaRepositories(basePackages = ["kr.hhplus.be.server.product.repository"])
@EntityScan(basePackages = ["kr.hhplus.be.server.product"])

class ServerApplication

fun main(args: Array<String>) {
	runApplication<ServerApplication>(*args)
}
