package io.teapot.application.config

import io.teapot.application.event.AsyncOrderCreatedEventListener
import io.teapot.application.event.OrderCreatedEventListener
import io.teapot.usecase.beverages.ServeOrder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
@EnableAsync
class AsyncConfiguration {

    @Bean
    fun orderCreatedEventListener(serveOrder: ServeOrder): OrderCreatedEventListener =
        AsyncOrderCreatedEventListener(serveOrder)

    @Bean
    fun taskExecutor(): TaskExecutor = ThreadPoolTaskExecutor()
}
