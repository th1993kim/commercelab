package com.commercelab.product.application.input

import com.commercelab.product.domain.model.Product

data class CreateProductCommand(
    val name: String,
    val description: String?,
    val price: Long
)