package com.commercelab.product.adapter.input.web.error

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "에러 응답")
data class ErrorResponse(
    @field:Schema(description = "에러 코드", example = "PRODUCT_NOT_FOUND")
    val errorCode: String?,

    @field:Schema(description = "에러 메시지", example = "상품을 찾을 수 없습니다. productId=1")
    val message: String?
)