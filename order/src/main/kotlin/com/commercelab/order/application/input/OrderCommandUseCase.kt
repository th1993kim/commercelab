package com.commercelab.order.application.input

interface OrderCommandUseCase {

    fun save(command: CreateOrderCommand): CreateOrderResult
}