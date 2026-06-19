package com.commercelab.product.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ProductTest {

    @Test
    fun `상품 변경 테스트`() {
        val newProduct = Product.create(
            name = "name",
            description = "description",
            price = 1000000
        )

        newProduct.update(
            name = "newName",
            description = null,
            price = 10,
            status = ProductStatus.DISCONTINUED
        )

        assertEquals("newName", newProduct.name)
        assertEquals(null, newProduct.description)
        assertEquals(10, newProduct.price)
        assertEquals(ProductStatus.DISCONTINUED, newProduct.status)
    }


    @Test
    fun `정상적인 상품 생성 테스트`() {
        val result = Product.create(
            name = "name",
            description = "description",
            price = 1000000
        )

        assertEquals("name", result.name)
        assertEquals("description", result.description)
        assertEquals(1000000, result.price)
        assertEquals(ProductStatus.ACTIVE, result.status)
    }

    @Test
    fun `상품명이 비어있을때 상품 생성불가 되어야한다`() {
        assertFailsWith<IllegalArgumentException> {
            Product.create(
                name = " ",
                description = "description",
                price = 1000000
            )
        }
    }

    @Test
    fun `금액이 음수로 지정되면 상품 생성 불가 되어야한다`() {
        assertFailsWith<IllegalArgumentException> {
            Product.create(
                name = "name",
                description = "description",
                price = -1
            )
        }
    }
}