package com.commercelab.product.adapter.input.web.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "상품 조회 응답")
data class GetProductResponse(
    @field:Schema(description = "상품 식별 번호", example = "1")
    val id: Long,

    @field:Schema(description = "상품명", example = "무선 마우스", nullable = true)
    val name: String,

    @field:Schema(description = "상품 설명", example = "블루투스 무선 마우스")
    val description: String?,

    @field:Schema(description = "상품 가격", example = "10000")
    val price: Long
)
