package com.commercelab.product.application.input

import com.commercelab.product.domain.model.ProductStatus

data class UpdateProductCommand(
    val id: Long,
    val name: String,
    val description: String?,
    val price: Long,
    val status: ProductStatus
)
