package com.commercelab.product.adapter.output

import com.commercelab.product.adapter.output.persistence.ProductJpaEntity
import com.commercelab.product.adapter.output.persistence.ProductJpaRepository
import com.commercelab.product.application.output.SaveProductPort
import com.commercelab.product.domain.model.Product
import org.springframework.stereotype.Repository

@Repository
class ProductPersistenceAdapter(
    private val productJpaRepository : ProductJpaRepository
) : SaveProductPort {
    override fun save(product: Product) {
        productJpaRepository.save(ProductJpaEntity.from(product))
    }
}