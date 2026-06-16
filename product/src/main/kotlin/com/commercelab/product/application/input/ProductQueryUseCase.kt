package com.commercelab.product.application.input

interface ProductQueryUseCase {

    fun getProduct(id: Long): GetProductResult?
}