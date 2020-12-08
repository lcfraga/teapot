package io.teapot.application.config

import io.teapot.dataprovider.repositories.BeveragesRepository
import io.teapot.dataprovider.repositories.OrdersRepository
import io.teapot.usecase.beverages.CreateBeverage
import io.teapot.usecase.beverages.DeleteBeverage
import io.teapot.usecase.beverages.FindAllBeverages
import io.teapot.usecase.beverages.FindAllOrders
import io.teapot.usecase.beverages.FindBeverage
import io.teapot.usecase.beverages.FindOrder
import io.teapot.usecase.beverages.OrderBeverage
import io.teapot.usecase.beverages.UpdateBeverage
import io.teapot.usecase.beverages.port.ClockPort
import io.teapot.usecase.beverages.port.GenerateIdPort
import io.teapot.usecase.beverages.port.TeapotPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCaseConfiguration {

    @Bean
    fun findAllBeverages(beveragesRepository: BeveragesRepository) = FindAllBeverages(beveragesRepository)

    @Bean
    fun findBeverage(beveragesRepository: BeveragesRepository) = FindBeverage(beveragesRepository)

    @Bean
    fun createBeverage(beveragesRepository: BeveragesRepository, generateIdPort: GenerateIdPort) = CreateBeverage(
        beveragesRepository,
        generateIdPort,
        beveragesRepository
    )

    @Bean
    fun updateBeverage(beveragesRepository: BeveragesRepository) = UpdateBeverage(
        beveragesRepository,
        beveragesRepository
    )

    @Bean
    fun deleteBeverage(beveragesRepository: BeveragesRepository) = DeleteBeverage(
        beveragesRepository,
        beveragesRepository
    )

    @Bean
    fun findAllOrders(ordersRepository: OrdersRepository) = FindAllOrders(ordersRepository)

    @Bean
    fun findOrder(ordersRepository: OrdersRepository) = FindOrder(ordersRepository)

    @Bean
    fun orderBeverage(
        beveragesRepository: BeveragesRepository,
        teapotPort: TeapotPort,
        generateIdPort: GenerateIdPort,
        clockPort: ClockPort,
        ordersRepository: OrdersRepository
    ) = OrderBeverage(
        beveragesRepository,
        teapotPort,
        generateIdPort,
        clockPort,
        ordersRepository
    )
}
