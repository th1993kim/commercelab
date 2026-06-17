package com.commercelab.product.application.exception

class ProductNotFoundException(
    val productId: Long,
) : RuntimeException("상품을 찾을 수 없습니다. productId=$productId")