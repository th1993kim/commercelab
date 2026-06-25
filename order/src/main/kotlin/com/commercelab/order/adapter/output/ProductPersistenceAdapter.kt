package com.commercelab.order.adapter.output

import com.commercelab.order.adapter.output.persistence.OrderJpaEntity
import com.commercelab.order.adapter.output.persistence.OrderJpaRepository
import com.commercelab.order.application.output.SaveOrderPort
import com.commercelab.order.domain.model.Order
import org.springframework.stereotype.Repository


@Repository
class ProductPersistenceAdapter(
    private val productJpaRepository: OrderJpaRepository
) : SaveOrderPort {

    override fun save(order: Order) {
        OrderJpaEntity.from(order);
    }

}