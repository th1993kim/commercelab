package com.commercelab.product.adapter.input.web

import com.commercelab.product.adapter.input.web.dto.CreateProductRequest
import com.commercelab.product.application.input.ProductCommandUseCase
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RequestMapping(value = ["/api/products"])
@RestController
class ProductController(
    private val productCommandUseCase: ProductCommandUseCase
) {

    @PostMapping
    fun createProduct(@RequestBody request: CreateProductRequest) {
        productCommandUseCase.createProduct(request.toCreateProductCommand())
    }

}

