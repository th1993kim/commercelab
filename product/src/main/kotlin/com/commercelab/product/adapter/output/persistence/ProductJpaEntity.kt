package com.commercelab.product.adapter.output.persistence

import com.commercelab.product.domain.model.Product
import com.commercelab.product.domain.model.ProductStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "product")
class ProductJpaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, length = 200)
    var name: String,
    @Column(nullable = true, length = 1000)
    var description: String? = null,
    @Column(nullable = false)
    var price: Long,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    var status: ProductStatus,

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,
    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null
) {
    companion object {
        fun from(product: Product) : ProductJpaEntity {
            return ProductJpaEntity(
                id = product.id,
                name = product.name,
                description = product.description,
                price = product.price,
                status = product.status
            )
        }
    }

    fun toDomain(): Product {
        return Product.restore(id = requireNotNull(id), name = name, description = description, price = price, status = status)
    }
}
