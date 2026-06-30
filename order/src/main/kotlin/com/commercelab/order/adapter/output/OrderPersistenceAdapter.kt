package com.commercelab.order.adapter.output

import com.commercelab.order.adapter.output.persistence.OrderJpaEntity
import com.commercelab.order.adapter.output.persistence.OrderJpaRepository
import com.commercelab.order.application.output.SaveOrderPort
import com.commercelab.order.domain.model.Order
import org.springframework.stereotype.Repository


@Repository
class OrderPersistenceAdapter(
    private val orderJpaRepository: OrderJpaRepository
) : SaveOrderPort {

    override fun save(order: Order) : Order {
        val orderEntity = OrderJpaEntity.from(order);
        orderJpaRepository.save(orderEntity);

        return orderEntity.toDomain();
    }

}