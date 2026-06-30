package com.commercelab.order.application.service

import com.commercelab.order.application.input.CreateOrderCommand
import com.commercelab.order.application.input.CreateOrderResult
import com.commercelab.order.application.input.OrderCommandUseCase
import com.commercelab.order.application.output.SaveOrderPort
import com.commercelab.order.domain.model.Order
import com.commercelab.order.domain.model.OrderItem
import com.commercelab.order.domain.model.OrderStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class OrderService (
    private val saveOrderPort: SaveOrderPort
) : OrderCommandUseCase {

    /*
        todo
        1. 주문번호 생성 (Sequence)
        2. 상품 가격 체크
        3. 재고 확인
        4. 재고 예약
     */
    @Transactional
    override fun save(command: CreateOrderCommand): CreateOrderResult{

        val order = Order(
            orderNumber = "1234",
            ordererId = command.ordererId,
            ordererName = command.ordererName,
            ordererEmail = command.ordererEmail,
            status = OrderStatus.CREATED,
            totalAmount = command.totalAmount,
            idempotencyKey = command.idempotencyKey,
            items = command.items.map { item ->
                OrderItem(
                    productId = item.productId,
                    productName = "name",
                    unitPrice = 100,
                    quantity = item.quantity,
                    paymentAmount = 100
                )
            }.toMutableList()
        )

        val savedOrder = saveOrderPort.save(order)

        return CreateOrderResult(
            id = savedOrder.id!!,
            orderNumber = savedOrder.ordererName,
            totalAmount = savedOrder.totalAmount,
            items = savedOrder.items.map { item ->
                CreateOrderResult.CreateOrderItemResult(
                    productId = item.productId,
                    productName = item.productName,
                    quantity = item.quantity,
                    unitPrice = item.unitPrice,
                    paymentAmount = item.paymentAmount
                )
            }
        )
    }

}