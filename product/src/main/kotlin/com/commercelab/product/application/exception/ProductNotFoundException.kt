package com.commercelab.product.application.exception

import com.commercelab.product.adapter.input.web.error.ErrorCode

class ProductNotFoundException(
    val productId: Long,
) : RuntimeException("상품을 찾을 수 없습니다. productId=$productId") {
    val errorCode: ErrorCode = ErrorCode.PRODUCT_NOT_FOUND
}