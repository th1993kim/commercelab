package com.commercelab.order.adapter.input.web.dto

import com.commercelab.order.application.input.CreateOrderCommand
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero

data class CreateOrderRequest(
    @field:NotBlank(message = "상품 구매자 고유번호는 비어 있을 수 없습니다.")
    val ordererId: String?,
    @field:NotBlank(message = "상품 구매자 명은 비어 있을 수 없습니다.")
    val ordererName: String?,
    val ordererEmail: String?,
    @field:NotNull(message = "결제 금액이 비어있을 수 없습니다.")
    @field:PositiveOrZero(message = "결제금액은 음수가 될 수 없습니다.")
    val totalAmount: Long?,
    val idempotencyKey: String?,
    @field:Valid
    val items: List<CreateOrderItemRequest>
) {
    fun toCommand(): CreateOrderCommand {
        return CreateOrderCommand(
            ordererId = ordererId!!,
            ordererName = ordererName!!,
            ordererEmail = ordererEmail,
            totalAmount = totalAmount!!,
            idempotencyKey = idempotencyKey!!,
            items = items.map { item -> item.toCommand() }
        )
    }

    data class CreateOrderItemRequest(
        @field:NotNull(message = "상품번호가 비어 있을 수 없습니다.")
        val productId: Long?,
        @field:NotNull(message = "결제 금액이 비어있을 수 없습니다.")
        @field:PositiveOrZero(message = "구매하는 상품의 갯수가 음수가 될 수 없습니다.")
        val quantity: Int?
    ) {
        fun toCommand(): CreateOrderCommand.CreateOrderItemCommand {
            return CreateOrderCommand.CreateOrderItemCommand(
                productId = productId!!,
                quantity = quantity!!
            )

        }
    }
}

