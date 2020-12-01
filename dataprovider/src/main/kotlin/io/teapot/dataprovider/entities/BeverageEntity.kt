package io.teapot.dataprovider.entities

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@TypeDefs(
    TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
)
@Entity
@Table(name = "beverages")
data class BeverageEntity(
    @Id
    val id: String,

    val name: String,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    val settings: Map<String, String> = emptyMap()
)
