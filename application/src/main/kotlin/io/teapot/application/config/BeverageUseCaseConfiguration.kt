package io.teapot.application.config

import io.teapot.dataprovider.repositories.BeveragesRepository
import io.teapot.dataprovider.repositories.JpaBeveragesRepository
import io.teapot.usecase.beverages.CreateBeverage
import io.teapot.usecase.beverages.DeleteBeverage
import io.teapot.usecase.beverages.FindAllBeverages
import io.teapot.usecase.beverages.FindBeverage
import io.teapot.usecase.beverages.UpdateBeverage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.UUID

@Configuration
class BeverageUseCaseConfiguration {

    @Bean
    fun findAllBeverages(beveragesRepository: BeveragesRepository) = FindAllBeverages(beveragesRepository)

    @Bean
    fun findBeverage(beveragesRepository: BeveragesRepository) = FindBeverage(beveragesRepository)

    @Bean
    fun createBeverage(beveragesRepository: BeveragesRepository) = CreateBeverage(
        beveragesRepository,
        { UUID.randomUUID().toString() },
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
    fun beveragesRepository(jpaBeveragesRepository: JpaBeveragesRepository) =
        BeveragesRepository(jpaBeveragesRepository)
}
