package io.teapot.dataprovider.entities

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@TypeDefs(
    TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
)
@Entity
@Table(name = "orders")
data class OrderEntity(
    @Id
    val id: String,

    val beverage: String,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    val settings: Map<String, String> = emptyMap(),

    val username: String,
    val size: String,
    val createdAt: Instant,
    val served: Boolean = false,
    val servedAt: Instant? = null
)
