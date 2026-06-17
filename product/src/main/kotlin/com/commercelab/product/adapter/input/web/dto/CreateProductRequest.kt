package com.commercelab.product.adapter.input.web.dto

import com.commercelab.product.application.input.CreateProductCommand
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero

@Schema(description = "상품 생성 요청")
data class CreateProductRequest(
    @field:NotBlank(message = "상품명은 비어 있을 수 없습니다.")
    @field:Schema(description = "상품명", example = "무선 마우스")
    val name: String,

    @field:Schema(description = "상품 설명", example = "블루투스 무선 마우스")
    val description: String?,

    @field:PositiveOrZero(message = "금액은 0원 이상 입력해야합니다.")
    @field:Schema(description = "상품 금액", example = "10000")
    val price: Long
) {
    fun toCreateProductCommand(): CreateProductCommand {
        return CreateProductCommand(name = name, description = description, price = price)
    }
}
