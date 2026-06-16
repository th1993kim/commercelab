package com.commercelab.product.adapter.input.web.dto

data class GetProductResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val price: Long
)
