package com.commercelab.product.application.input

data class GetProductResult(
    val id: Long,
    val name: String,
    val description: String?,
    val price: Long
)
