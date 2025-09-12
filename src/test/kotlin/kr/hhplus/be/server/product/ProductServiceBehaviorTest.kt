import com.github.dockerjava.api.exception.NotFoundException
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.*
import kr.hhplus.be.server.product.controller.dto.ProductDetailRes
import kr.hhplus.be.server.product.repository.Product
import kr.hhplus.be.server.product.repository.ProductJpa
import kr.hhplus.be.server.product.service.ProductService
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

class ProductServiceBehaviorTest : BehaviorSpec({

    val productJpa = mockk<ProductJpa>()
    val productService = ProductService(productJpa)

    beforeTest {
        clearMocks(productJpa, recordedCalls = true, answers = false)
    }

    given("상품 상세 조회 로직") {

        `when`("해당 상품 ID에 대한 정보가 존재하면") {
            lateinit var productDetailRes: ProductDetailRes

            beforeTest {
                every { productJpa.findById(1L).getOrNull() } returns Product(
                    id = 1L,
                    category = Product.Category.UNIFORM,
                    name = "농구 유니폼",
                    price = BigDecimal("10000.00"),
                    discountPrice = BigDecimal("9000.00"),
                    stock = 50,
                    cumulativeSaleCount = 200,
                    description = "신상 농구 유니폼",
                    state = Product.State.ACTIVE,
                    createdAt = LocalDateTime.parse("2025-10-01T10:15:30")
                )

                productDetailRes = productService.getProductDetail(1L)
            }

            then("상품 상세 정보가 반환되어야 한다") {
                productDetailRes.id shouldBe 1L
                productDetailRes.name shouldBe "농구 유니폼"
                productDetailRes.price shouldBe BigDecimal("10000.00")
                productDetailRes.discountPrice shouldBe BigDecimal("9000.00")
                productDetailRes.stock shouldBe 50
                productDetailRes.cumulativeSaleCount shouldBe 200
                productDetailRes.description shouldBe "신상 농구 유니폼"
                productDetailRes.state shouldBe Product.State.ACTIVE
                productDetailRes.createdAt shouldBe LocalDateTime.parse("2025-10-01T10:15:30")
            }

            then("JPA의 findById가 1회 호출되어야 한다") {
                verify(exactly = 1) { productJpa.findById(1L) }
                confirmVerified(productJpa)
            }
        }

        `when`("해당 상품 ID에 대한 정보가 존재하지 않으면") {

            beforeTest {
                every { productJpa.findById(2L).getOrNull() } returns null
            }

            then("NotFoundException이 발생해야 한다") {
                val exception = shouldThrow<NotFoundException> {
                    productService.getProductDetail(2L)
                }

                exception::class shouldBe NotFoundException::class

                verify(exactly = 1) { productJpa.findById(2L) }
                confirmVerified(productJpa)
            }
        }
    }
})
