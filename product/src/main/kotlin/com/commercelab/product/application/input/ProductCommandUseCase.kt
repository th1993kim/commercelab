package com.commercelab.product.application.input

interface ProductCommandUseCase {

    fun createProduct(command: CreateProductCommand)
    fun updateProduct(command: UpdateProductCommand)
}