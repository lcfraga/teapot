package io.teapot.dataprovider.repositories

import io.teapot.dataprovider.entities.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaOrdersRepository : JpaRepository<OrderEntity, String>
