package com.commercelab.product.adapter.input.web.dto

import com.commercelab.product.application.input.CreateProductCommand

data class CreateProductRequest(
    val name: String,
    val description: String?,
    val price: Long
) {
    fun toCreateProductCommand(): CreateProductCommand {
        return CreateProductCommand(name = name, description = description, price = price)
    }
}
