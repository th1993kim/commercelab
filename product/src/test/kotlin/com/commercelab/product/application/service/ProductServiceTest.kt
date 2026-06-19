package com.commercelab.product.application.service

import com.commercelab.product.application.exception.ProductNotFoundException
import com.commercelab.product.application.input.CreateProductCommand
import com.commercelab.product.application.input.UpdateProductCommand
import com.commercelab.product.application.output.LoadProductPort
import com.commercelab.product.application.output.SaveProductPort
import com.commercelab.product.domain.model.Product
import com.commercelab.product.domain.model.ProductStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class ProductServiceTest {
    private val productRepository = FakeProductRepository()
    private val productService = ProductService(
        saveProductPort = productRepository,
        loadProductPort = productRepository
    )

    @Test
    fun `상품이 존재할때 상품 데이터 변경 테스트`() {
        productRepository.productMap[1L] = Product.restore(
            id = 1L,
            name = "name",
            description = "description",
            price = 1000000,
            status = ProductStatus.ACTIVE
        )
        productService.updateProduct(UpdateProductCommand(
            id = 1L,
            name = "newName",
            description = null,
            price = 10,
            status = ProductStatus.DISCONTINUED
        ))

        val product = productRepository.productMap.values.single()
        assertNotNull(product)
        assertEquals(product.id, 1L)
        assertEquals(product.name, "newName")
        assertEquals(product.description, null)
        assertEquals(product.price, 10)
        assertEquals(product.status, ProductStatus.DISCONTINUED)

    }

    @Test
    fun `상품이 없을때 예외 호출 테스트`() {
        assertFailsWith<ProductNotFoundException>{productService.updateProduct(UpdateProductCommand(
            id = 1L,
            name = "newName",
            description = null,
            price = 10,
            status = ProductStatus.DISCONTINUED
        ))}
    }

    @Test
    fun `상품 저장 테스트`() {
        productService.createProduct(
            command = CreateProductCommand(
                name = "name",
                description = "description",
                price = 1000000
            )
        )

        val product = productRepository.productMap.values.single()
        assertNotNull(product)
        assertEquals(product.id, 1L)
        assertEquals(product.name, "name")
        assertEquals(product.description, "description")
        assertEquals(product.price, 1000000)
        assertEquals(product.status, ProductStatus.ACTIVE)
    }

    @Test
    fun `상품 조회 테스트`() {
        productRepository.productMap[1L] = Product.restore(
            id = 1L,
            name = "name",
            description = "description",
            price = 1000000,
            ProductStatus.ACTIVE
        )

        val product = productService.getProduct(id = 1L);
        assertNotNull(product)
        assertEquals(product.id, 1L)
        assertEquals(product.name, "name")
        assertEquals(product.description, "description")
        assertEquals(product.price, 1000000)
    }

    @Test
    fun `조회할 상품이 없는 경우 예외 발생 테스트`() {
        assertFailsWith<ProductNotFoundException> {
            productService.getProduct(id = 1L)
        }
    }

    private class FakeProductRepository : SaveProductPort, LoadProductPort {
        val productMap = mutableMapOf<Long, Product>();
        private var sequence = 1L;

        override fun save(product: Product) {
            var id = product.id ?: sequence++
            productMap[id] = Product.restore(
                id = id,
                name = product.name,
                description = product.description,
                price = product.price,
                status = product.status
            )
        }

        override fun load(id: Long): Product? {
            return productMap[id]
        }

    }

}