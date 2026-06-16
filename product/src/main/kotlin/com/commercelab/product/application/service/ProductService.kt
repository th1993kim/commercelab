package com.commercelab.product.application.service

import com.commercelab.product.application.input.CreateProductCommand
import com.commercelab.product.application.input.GetProductResult
import com.commercelab.product.application.input.ProductCommandUseCase
import com.commercelab.product.application.input.ProductQueryUseCase
import com.commercelab.product.application.output.LoadProductPort
import com.commercelab.product.application.output.SaveProductPort
import com.commercelab.product.domain.model.Product
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductService(
    private val saveProductPort: SaveProductPort,
    private val loadProductPort: LoadProductPort
) : ProductCommandUseCase, ProductQueryUseCase {


    @Transactional
    override fun createProduct(command: CreateProductCommand) {
        saveProductPort.save(Product.create(
            name = command.name,
            description = command.description,
            price = command.price
        ))
    }

    override fun getProduct(id: Long): GetProductResult? {
        val product = loadProductPort.load(id)
        return product?.let { item -> GetProductResult(
            id = requireNotNull(item.id),
            name = item.name,
            description = item.description,
            price = item.price
        )}
    }
}