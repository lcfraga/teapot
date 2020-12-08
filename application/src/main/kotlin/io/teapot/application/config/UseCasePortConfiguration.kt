package io.teapot.application.config

import io.teapot.dataprovider.repositories.BeveragesRepository
import io.teapot.dataprovider.repositories.JpaBeveragesRepository
import io.teapot.dataprovider.repositories.JpaOrdersRepository
import io.teapot.dataprovider.repositories.OrdersRepository
import io.teapot.domain.entity.Order
import io.teapot.domain.entity.OrderSize
import io.teapot.usecase.beverages.port.ClockPort
import io.teapot.usecase.beverages.port.GenerateIdPort
import io.teapot.usecase.beverages.port.TeapotPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.util.UUID
import kotlin.random.Random

@Configuration
class UseCasePortConfiguration {

    @Bean
    fun beveragesRepository(jpaBeveragesRepository: JpaBeveragesRepository) =
        BeveragesRepository(jpaBeveragesRepository)

    @Bean
    fun ordersRepository(jpaOrdersRepository: JpaOrdersRepository) =
        OrdersRepository(jpaOrdersRepository)

    @Bean
    fun generateIdPort(): GenerateIdPort = GenerateIdPort { UUID.randomUUID().toString() }

    @Bean
    fun teapotPort(): TeapotPort = object : TeapotPort {

        override fun hasEnoughWaterFor(orderSize: OrderSize): Boolean =
            Random.nextInt(0, 10) != 9

        override fun canBrew(beverage: String): Boolean =
            listOf(
                "chai tea",
                "green tea",
                "english breakfast",
                "earl grey"
            ).contains(beverage.toLowerCase())

        override fun brew(order: Order) {
            // Do nothing.
        }
    }

    @Bean
    fun clockPort(): ClockPort = ClockPort { Instant.now() }
}
