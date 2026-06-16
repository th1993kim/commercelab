package com.commercelab.product.adapter.input.web

import com.commercelab.product.adapter.input.web.dto.CreateProductRequest
import com.commercelab.product.adapter.input.web.dto.GetProductResponse
import com.commercelab.product.application.input.ProductCommandUseCase
import com.commercelab.product.application.input.ProductQueryUseCase
import org.springframework.web.bind.annotation.*

@RequestMapping(value = ["/api/products"])
@RestController
class ProductController(
    private val productCommandUseCase: ProductCommandUseCase,
    private val productQueryUseCase: ProductQueryUseCase
) {

    @PostMapping
    fun createProduct(@RequestBody request: CreateProductRequest) {
        productCommandUseCase.createProduct(request.toCreateProductCommand())
    }

    @GetMapping("/{productId}")
    fun getProduct(@PathVariable productId: Long) : GetProductResponse? {
        val productResult = productQueryUseCase.getProduct(productId);
        return GetProductResponse(
            id = productResult.id,
            name = productResult.name,
            description = productResult.description,
            price = productResult.price
        )
    }

}

