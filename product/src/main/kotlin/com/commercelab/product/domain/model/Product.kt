package com.commercelab.product.domain.model

class Product private constructor(
    val id: Long? = null,
    name: String,
    description: String? = null,
    price: Long,
    status: ProductStatus
) {
    companion object {
        fun create(
            name: String,
            description: String?,
            price: Long,
        ) : Product {
            return Product(name = name, description = description, price = price, status = ProductStatus.ACTIVE)
        }

        fun restore(
            id: Long,
            name: String,
            description: String?,
            price: Long,
            status: ProductStatus
        ): Product {
            return Product(id = id, name = name, description = description, price = price, status = status)
        }
    }
    var name: String = name
        private set

    var description: String? = description
        private set

    var price: Long = price
        private set

    var status: ProductStatus = status
        private set

    init {
        require(name.isNotBlank()) {"상품명은 비어 있을 수 없습니다."}
        require(price >= 0) {"상품 가격은 음수일 수 없습니다."}
    }
}
