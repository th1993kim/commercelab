package com.commercelab.product.application.service

import com.commercelab.product.application.input.CreateProductCommand
import com.commercelab.product.application.input.ProductCommandUseCase
import com.commercelab.product.application.output.SaveProductPort
import com.commercelab.product.domain.model.Product
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductService(
    private val saveProductPort: SaveProductPort
) : ProductCommandUseCase {


    @Transactional
    override fun createProduct(command: CreateProductCommand) {
        saveProductPort.save(Product.create(
            name = command.name,
            description = command.description,
            price = command.price
        ))
    }
}