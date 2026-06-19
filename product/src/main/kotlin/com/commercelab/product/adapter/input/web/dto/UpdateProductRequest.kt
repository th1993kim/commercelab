package com.commercelab.product.adapter.input.web.dto

import com.commercelab.product.application.input.UpdateProductCommand
import com.commercelab.product.domain.model.ProductStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero

@Schema(description = "상품 수정 요청")
data class UpdateProductRequest(
    @field:NotBlank(message = "상품명이 비어있어서는 안됩니다.")
    @field:Schema(description = "상품명", example = "무선 마우스")
    val name: String,
    @field:Schema(description = "상품 설명", example = "블루투스 무선 마우스")
    val description: String?,
    @field:PositiveOrZero(message = "금액은 0원 이상 입력해야합니다.")
    @field:Schema(description = "상품 금액", example = "10000")
    val price: Long,
    @field:NotNull(message = "상품의 상태가 비어있어서는 안됩니다.")
    @field:Schema(description = "ACTIVE: 판매, DISCONTINUED: 판매중지", example = "DISCONTINUED")
    val status: ProductStatusRequest
) {
    fun toUpdateProductCommand(id: Long) = UpdateProductCommand(
        id = id,
        name = name,
        description = description,
        price = price,
        status = status.toDomain()

    )

    enum class ProductStatusRequest {
        ACTIVE,
        DISCONTINUED;

        fun toDomain(): ProductStatus =
            when (this) {
                ACTIVE -> ProductStatus.ACTIVE
                DISCONTINUED -> ProductStatus.DISCONTINUED
            }
    }
}
