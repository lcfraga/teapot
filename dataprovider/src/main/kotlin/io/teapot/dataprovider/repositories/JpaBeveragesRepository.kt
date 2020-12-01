package io.teapot.dataprovider.repositories

import io.teapot.dataprovider.entities.BeverageEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface JpaBeveragesRepository : JpaRepository<BeverageEntity, String> {
    fun findByNameIgnoreCase(name: String): Optional<BeverageEntity>
}
